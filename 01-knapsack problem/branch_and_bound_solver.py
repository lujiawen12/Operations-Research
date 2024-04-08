from base_solver import BaseSolver


class KnapsackNode(object):
    def __init__(self, items, depth, weight, value):
        self.items = items
        self.depth = depth
        self.weight = weight
        self.value = value


class BBSolver(BaseSolver):
    def __init__(self, input_data):
        super().__init__(input_data)

    def run(self):
        # parse the input
        total_items, capacity = self._parse_input()

        # algorithm
        total_items.sort(key=lambda x: x.value / (x.weight + 0.0), reverse=True)
        best_val = initialize(total_items, capacity)
        taken = [0] * len(total_items)

        queue = [KnapsackNode([], 0, 0, 0)]
        while queue:
            node = queue.pop(0)
            print(node.depth)
            if node.depth == len(total_items):
                if node.value > best_val:
                    best_val = node.value
                    taken = [0] * len(total_items)
                    for item in node.items:
                        taken[item.index] = 1
                    print(best_val)

                continue

            cur_item = total_items[node.depth]
            with_item = KnapsackNode(node.items + [cur_item],
                                     node.depth + 1,
                                     node.weight + cur_item.weight,
                                     node.value + cur_item.value)
            if is_promising(with_item, capacity, best_val, total_items):
                queue.append(with_item)

            without_item = KnapsackNode(node.items,
                                        node.depth + 1,
                                        node.weight,
                                        node.value)
            if is_promising(without_item, capacity, best_val, total_items):
                queue.append(without_item)

        # prepare the solution in the specified output format
        output_data = str(best_val) + ' ' + str(0) + '\n'
        output_data += ' '.join(map(str, taken))
        return output_data


def initialize(total_items, capacity):
    weight = 0
    value = 0
    for item in total_items:
        if weight + item.weight <= capacity:
            value += item.value
            weight += item.weight
    return value


def is_promising(node: KnapsackNode, capacity, best_val, total_items):
    if node.weight <= capacity and cal_upper_bound(node, total_items, capacity) > best_val:
        return True
    return False


def cal_upper_bound(node: KnapsackNode, total_items, capacity):
    upper_bound = node.value
    weight = node.weight
    for i in range(node.depth, len(total_items)):
        if weight >= capacity:
            break
        if weight + total_items[i].weight <= capacity:
            upper_bound += total_items[i].value
        else:
            upper_bound += (capacity - weight) * total_items[i].value / total_items[i].weight
        weight += total_items[i].weight
    return upper_bound

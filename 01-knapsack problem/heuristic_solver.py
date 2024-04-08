from base_solver import BaseSolver


class HeuristicSolver(BaseSolver):
    def __init__(self, input_data):
        super().__init__(input_data)

    def run(self):
        # parse the input
        items, capacity = self._parse_input()

        # a trivial algorithm for filling the knapsack
        # it takes items in-order until the knapsack is full
        value = 0
        weight = 0
        taken = [0] * len(items)

        items.sort(key=lambda x: x.value / (x.weight + 0.0), reverse=True)
        for item in items:
            if weight + item.weight <= capacity:
                taken[item.index] = 1
                value += item.value
                weight += item.weight

        # prepare the solution in the specified output format
        output_data = str(value) + ' ' + str(0) + '\n'
        output_data += ' '.join(map(str, taken))
        return output_data

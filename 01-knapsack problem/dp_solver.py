import numpy as np

from base_solver import BaseSolver


class DPSolver(BaseSolver):
    def __init__(self, input_data):
        super().__init__(input_data)

    def run(self):
        # parse the input
        items, capacity = self._parse_input()

        # main body
        value, taken = self.simple_dp(items, capacity)

        # prepare the solution in the specified output format
        output_data = str(value) + ' ' + str(0) + '\n'
        output_data += ' '.join(map(str, taken))
        return output_data

    @staticmethod
    def simple_dp(items, capacity):
        row_num = len(items) + 1
        col_num = capacity + 1
        dp_mat = np.zeros([row_num, col_num], dtype=int)
        for i, item in enumerate(items):
            i += 1
            weight = item.weight
            for j in range(1, capacity + 1):
                if j < weight:
                    dp_mat[i, j] = dp_mat[i - 1, j]
                else:
                    dp_mat[i, j] = max(dp_mat[i - 1, j], dp_mat[i - 1, j - weight] + item.value)

        taken = [0] * len(items)
        i, j = dp_mat.shape
        i -= 1
        j -= 1
        value = dp_mat[i, j]
        while i > 0 and j > 0:
            if dp_mat[i, j] != dp_mat[i - 1, j]:
                item_idx = i - 1
                taken[item_idx] = 1
                j -= items[item_idx].weight
            i -= 1

        return value, taken

    @staticmethod
    def compress_dp(items, capacity):
        col_num = capacity + 1
        dp_mat = [0] * col_num
        for item_idx, item in enumerate(items):
            weight = item.weight
            for k in range(capacity, weight - 1, -1):
                with_item_val = dp_mat[k - weight] + item.value
                without_item_val = dp_mat[k]
                dp_mat[k] = max(with_item_val, without_item_val)

        value = dp_mat[-1]
        return value


if __name__ == '__main__':
    file_location = 'data/ks_30_0'
    with open(file_location, 'r') as input_data_file:
        test_input_data = input_data_file.read()
    solver = DPSolver(test_input_data)
    out = solver.run()
    print(out)
    print('Happy Ending')

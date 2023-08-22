from abc import abstractmethod, ABCMeta

from collections import namedtuple

Item = namedtuple("Item", ['index', 'value', 'weight'])


class BaseSolver(metaclass=ABCMeta):
    def __init__(self, input_data):
        self.input_data = input_data

    def _parse_input(self):
        # parse the input
        lines = self.input_data.split('\n')

        first_line = lines[0].split()
        item_count = int(first_line[0])
        capacity = int(first_line[1])

        items = []
        for i in range(1, item_count + 1):
            line = lines[i]
            parts = line.split()
            items.append(Item(i - 1, int(parts[0]), int(parts[1])))

        return items, capacity

    @abstractmethod
    def run(self) -> str:
        pass

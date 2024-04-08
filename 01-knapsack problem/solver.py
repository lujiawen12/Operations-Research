#!/usr/bin/python
# -*- coding: utf-8 -*-

from heuristic_solver import HeuristicSolver
from branch_and_bound_solver import BBSolver
from dp_solver import DPSolver


def solve_it(input_data):
    solver = HeuristicSolver(input_data)
    # solver = BBSolver(input_data)
    # solver = DPSolver(input_data)
    return solver.run()


if __name__ == '__main__':
    import sys

    if len(sys.argv) > 1:
        file_location = sys.argv[1].strip()
        with open(file_location, 'r') as input_data_file:
            input_data = input_data_file.read()
        print(solve_it(input_data))
    else:
        print('This test requires an input file.  Please select one from the data directory. '
              '(i.e. python solver.py ./data/ks_4_0)')

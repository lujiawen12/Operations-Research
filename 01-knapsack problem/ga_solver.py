import copy
import random
from typing import List

import numpy as np

from base_solver import BaseSolver


class Individual(object):
    def __init__(self, perm: List[int], fitness, idx_bound):
        self.perm = perm
        self.idx_bound = idx_bound
        self.fitness = fitness


class GASolver(BaseSolver):
    def __init__(self, input_data):
        super().__init__(input_data)
        self.items, self.capacity = self._parse_input()

        # 遗传算法参数
        self.POP_SIZE = 100
        self.GEN_MAX = 1

        self.best_indv = Individual([], 0, 0)

    def run(self):
        population = self._initialize(self.POP_SIZE)
        for g in range(self.GEN_MAX):
            population.sort(key=lambda x: x.fitness, reverse=True)
            if population[0].fitness > self.best_indv.fitness:
                self.best_indv = copy.deepcopy(population[0])

            print(f'Generation {g}: {self.best_indv.fitness}.')

            # 进化
            population = self._evolve_population(population)

        # prepare the solution in the specified output format
        taken = [0] * len(self.items)
        for i in range(self.best_indv.idx_bound):
            taken[self.best_indv.perm[i]] = 1
        output_data = str(self.best_indv.fitness) + ' ' + str(0) + '\n'
        output_data += ' '.join(map(str, taken))
        return output_data

    def cal_fitness(self, perm):
        weight = 0
        fitness = 0
        bound = 0
        for idx, item_idx in enumerate(perm):
            weight += self.items[item_idx].weight
            if weight > self.capacity:
                bound = idx
                break
            fitness += self.items[item_idx].value

        return fitness, bound

    def _initialize(self, pop_size):
        population = []
        for i in range(pop_size):
            perm = [idx for idx in range(len(self.items))]
            random.shuffle(perm)
            fitness, bound = self.cal_fitness(perm)
            indv = Individual(perm, fitness, bound)
            population.append(indv)
        return population

    def _crossover(self, p1: List[int], p2: List[int]):
        item_num = len(self.items)
        sequence = np.arange(item_num)
        result = np.random.choice(sequence, size=2, replace=False)
        left_idx = np.min(result)
        right_idx = np.max(result)

        p1_key_part = set([p1[i] for i in range(left_idx, right_idx)])
        p2_key_part = set([p2[i] for i in range(left_idx, right_idx)])
        o1 = [0] * item_num
        o2 = [0] * item_num
        for i in range(left_idx, right_idx):
            o1[i] = p2[i]
            o2[i] = p1[i]

        cur_idx = 0
        for idx, val in enumerate(p1):
            if cur_idx == left_idx:
                cur_idx = right_idx
            if val in p2_key_part:
                continue
            o1[cur_idx] = val
            cur_idx += 1

        cur_idx = 0
        for idx, val in enumerate(p2):
            if cur_idx == left_idx:
                cur_idx = right_idx
            if val in p1_key_part:
                continue
            o2[cur_idx] = val
            cur_idx += 1

        o1_fitness, o1_bound = self.cal_fitness(o1)
        o1_indv = Individual(o1, o1_fitness, o1_bound)
        o2_fitness, o2_bound = self.cal_fitness(o2)
        o2_indv = Individual(o2, o2_fitness, o2_bound)
        return o1_indv, o2_indv

    # todo
    def _local_search(self, individual: Individual):
        pass

    def _evolve_population(self, population: List[Individual]):
        fitness_list = [indv.fitness for indv in population]
        fit_arr = np.array(fitness_list)
        p_arr = fit_arr / np.sum(fit_arr)
        pop_idx_list = [i for i in range(len(population))]

        new_population = []
        while len(new_population) < len(population):
            sel_idx_list = np.random.choice(pop_idx_list, size=2, replace=False, p=p_arr)
            o1_indv, o2_indv = self._crossover(population[sel_idx_list[0]].perm, population[sel_idx_list[1]].perm)
            self._local_search(o1_indv)
            self._local_search(o2_indv)
            new_population.append(o1_indv)
            new_population.append(o2_indv)

        return new_population


if __name__ == '__main__':
    file_location = 'data/ks_30_0'
    with open(file_location, 'r') as input_data_file:
        test_input_data = input_data_file.read()
    solver = GASolver(test_input_data)
    out = solver.run()
    print(out)
    print('Happy Ending')

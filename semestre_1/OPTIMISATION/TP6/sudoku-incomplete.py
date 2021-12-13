#!/usr/bin/env python3
# -*- coding: utf-8 -*-
import itertools
import math
import argparse
from subprocess import run

"""Sudoku"""

def var(i,j,k):
    """Return the literal Xijk.
    """
    return (1,i,j,k)

def neg(l):
    """Return the negation of the literal l.
    """
    (s,i,j,k) = l
    return (-s,i,j,k)

def initial_configuration():
    """Return the initial configuration of the example in td6.pdf
    
    >>> cnf = initial_configuration()
    >>> (1, 1, 4, 4) in cnf
    True
    >>> (1, 2, 1, 2) in cnf
    True
    >>> (1, 2, 3, 1) in cnf
    False
    """
    config = [
        var(1, 4, 4),
        var(2, 1, 2),
        var(3, 2, 1),
        var(4, 3, 1)
    ]
    return config # à compléter

def at_least_one(L):
    """Return a cnf that represents the constraint: at least one of the
    literals in the list L is true.
    
    >>> lst = [var(1, 1, 1), var(2, 2, 2), var(3, 3, 3)]
    >>> cnf = at_least_one(lst)
    >>> len(cnf)
    1
    >>> clause = cnf[0]
    >>> len(clause)
    3
    >>> clause.sort()
    >>> clause == [var(1, 1, 1), var(2, 2, 2), var(3, 3, 3)]
    True
    """
    return [L] # à compléter

def at_most_one(L):
    """Return a cnf that represents the constraint: at most one of the
    literals in the list L is true
    
    >>> lst = [var(1, 1, 1), var(2, 2, 2), var(3, 3, 3)]
    >>> cnf = at_most_one(lst)
    >>> len(cnf)
    3
    >>> cnf[0].sort()
    >>> cnf[1].sort()
    >>> cnf[2].sort()
    >>> cnf.sort()
    >>> cnf == [[neg(var(1,1,1)), neg(var(2,2,2))], \
    [neg(var(1,1,1)), neg(var(3,3,3))], \
    [neg(var(2,2,2)), neg(var(3,3,3))]]
    True
    """
    return [[neg(x), neg(y)] for (x, y) in itertools.combinations(L, 2)] # à compléter

def assignment_rules(N):
    """Return a list of clauses describing the rules for the assignment (i,j) -> k.
    """
    cnf = []
    for i in range(1,N+1):
        for j in range(1,N+1):
            positions = [var(i, j, k) for k in range(1,N+1)]
            for elt in at_least_one(positions):
                cnf.append(elt)
            for elt in at_most_one(positions):
                cnf.append(elt)
    return cnf

def row_rules(N):
    """Return a list of clauses describing the rules for the rows.
    """
    cnf = []
    for i in range(1,N+1):
        for k in range(1,N+1):
            positions = [var(i, j, k) for j in range(1,N+1)]
            for elt in at_least_one(positions):
                cnf.append(elt)
            for elt in at_most_one(positions):
                cnf.append(elt)
    return cnf # à compléter

def column_rules(N):
    """Return a list of clauses describing the rules for the columns.
    """
    cnf = []
    for j in range(1,N+1):
        for k in range(1,N+1):
            positions = [var(i, j, k) for i in range(1,N+1)]
            for elt in at_least_one(positions):
                cnf.append(elt)
            for elt in at_most_one(positions):
                cnf.append(elt)
    return cnf # à compléter

def block(x, y, k, size):
    return [var(x + i, y + j, k) for i in range(size) for j in range(size)]

def subgrid_rules(N):
    """Return a list of clauses describing the rules for the subgrids.
    """
    cnf = []
    step = math.ceil(math.sqrt(N))
    for k in range(1,N+1):
        for x in range(1,N+1, step):
            for y in range(1,N+1, step):
                positions = block(x, y, k, step)
                for elt in at_least_one(positions):
                    cnf.append(elt)
                for elt in at_most_one(positions):
                    cnf.append(elt)
    return cnf # à compléter

def generate_rules(N):
    """Return a list of clauses describing the rules of the game.
    """
    cnf = []    
    cnf.extend(assignment_rules(N))
    cnf.extend(row_rules(N))
    cnf.extend(column_rules(N))
    cnf.extend(subgrid_rules(N))
    return cnf

def literal_to_integer(l, N) -> int:
    """Return the external representation of the literal l.

    >>> literal_to_integer(var(1,2,3), 4)
    7
    >>> literal_to_integer(neg(var(3,2,1)), 4)
    -37
    """
    (s,i,j,k) = l[0], l[1], l[2], l[3]
    return s * (N**2 * (i - 1) + N * (j - 1) + k)

def parse_arguments():
    parser = argparse.ArgumentParser(description='Generate CNF file')
    parser.add_argument('output', type=str, default='output.cnf', help="Output LP file generated from your template")
    return parser.parse_args()

class generator():
    def __init__(self, output, rules:list, N:int):
        self.output = output
        self.rules = rules
        self.N = N

    def generate_start(self) -> None:
        init_config = initial_configuration()
        line = ["p", "cnf", str(self.N**3), str(len(self.rules) +
            len(init_config))]
        self.output.write(" ".join(line))
        self.output.write("\n")
        # Add config clause
        for config in init_config:
            self.output.write(' '.join([str(literal_to_integer(config, self.N)),
                '0', '\n']))

    def generate_middle(self) -> None:
        for clause in self.rules:
            line = [str(literal_to_integer(elt, self.N)) for elt in clause]
            line += str(0)
            self.output.write(' '.join(line) + '\n')

    # Generate cnf file
    def generate(self):
        self.generate_start()
        self.generate_middle()

def run_minisat(filename:str):
    run(['minisat', filename, 'result.out'])

def read_result(filename, N):
    f = open(filename, "r")
    data = f.readlines()[1].split()
    index = 0
    for _ in range(1,N+1):
        for _ in range(1,N+1):
            for value in range(1,N+1):
                if int(data[index]) > 0:
                    print(value, end="")
                index += 1
            print(" ", end="")
        print()

def main():
    import doctest
    doctest.testmod()

    arguments = parse_arguments()
    f = open(arguments.output, 'w')
    N = 4
    cnf = generate_rules(N)
    g = generator(f, cnf, N)
    g.generate()
    f.close()
    run_minisat(arguments.output)
    read_result("result.out", N)

if __name__ == "__main__":
    main()


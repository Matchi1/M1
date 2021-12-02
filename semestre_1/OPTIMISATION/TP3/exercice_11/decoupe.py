import argparse
from subprocess import run

def decoupe(maximum, elements):
    element = elements[0]
    max_iter = int(maximum / element)
    if len(elements) == 1:
        return [[max_iter]]
    result = []
    for i in range(max_iter + 1):
        const = element * i
        values = decoupe(maximum - const, elements[1:])
        for elt in values:
            elt.insert(0, i)
            result.append(elt)
    return result

def print_cut(values):
    print('List of combinations: ')
    print(str(values))
    print('Number of combinations: ')
    print(str(len(values)))

def parse_cut(maximum, all_cut, elements):
    f = open('data.txt', 'w')
    f.write(str(len(all_cut)) + ' ' + str(len(elements)) + '\n')
    line = []
    for elt in elements:
        line.append(str(elt))
    f.write(' '.join(line) + '\n')
    for i in range(len(all_cut)):
        combination = all_cut[i]
        line = []
        message = 'P' + str(i) + ' '
        for j in range(len(combination)):
            message += str(combination[j]) + ' '
        message += '1\n'
        f.write(message)

def parse_arguments():
    parser = argparse.ArgumentParser(description='Cut element combination')
    parser.add_argument('-l', '--list', nargs='+', default=[], type=int,
            help="Input for your template", required=True)
    parser.add_argument('-e', '--elements', nargs='+', default=[], type=int,
            help="Input for your template", required=True)
    parser.add_argument('max', type=int)
    return parser.parse_args()

arguments = parse_arguments()
segments = arguments.list
elements = arguments.elements
values = decoupe(arguments.max, segments)
print_cut(values)
parse_cut(arguments.max, values, elements)
run(['python3', 'generic.py', 'data.txt', 'output.lp', '-int', '-min'])

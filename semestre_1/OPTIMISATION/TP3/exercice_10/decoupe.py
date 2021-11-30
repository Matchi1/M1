import argparse

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

def parse_arguments():
    parser = argparse.ArgumentParser(description='Cut element combination')
    parser.add_argument('-l', '--list', nargs='+', default=[], type=int,
            help="Input for your template", required=True)
    parser.add_argument('max', type=int)
    return parser.parse_args()

arguments = parse_arguments()
segments = sorted(arguments.list, reverse=True)
value = decoupe(arguments.max, segments)
print('List of combinations: ')
print(str(value))
print('Number of combinations: ')
print(str(len(value)))

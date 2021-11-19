import argparse

def parse_argument():
    parser = argparse.ArgumentParser()
    parser.add_argument('data')
    parser.add_argument('lp')
    parser.add_argument('-int', action='store_true')
    return parser.parse_args()

def read_line_one(line):
    word = line.split(' ')
    return int(word[0]), int(word[1])

def read_line_two(line):
    word = line.split(' ')
    return [int(w) for w in word]

def read_other(line, nb_line):
    word = line.split(' ')
    values = [word[i + 1] for i in range(0, nb_line)]
    return word[0], values, word[-1]

def write_max(f, products, maxi):
    messages = "max: "
    for i in range(len(products)):
        messages += str(maxi[products[i]]).strip('\n') + products[i]
        if i < len(products) - 1:
            messages += '+'
    messages += ";\n" 
    f.write(messages)

def write_others(f, products, B, values, m):
    messages = ""
    for i in range(m):
        for j in range(len(products)):
            messages += (values[products[j]])[i] + products[j]
            if j < len(products) - 1:
                messages += "+"
        messages += " <= " + str(B[i]) + ";\n"
    f.write(messages)

def main():
    argument = parse_argument()
    n, m = 0, 0
    with open(argument.data) as f:
        lines = f.readlines()
        m, n = read_line_one(lines[0])
        B = read_line_two(lines[1])
        products = []
        values = {}
        maxi = {}
        for i in range(2, len(lines)):
            name, value, fin = read_other(lines[i], m)
            products.append(name)
            values[name] = value
            maxi[name] = fin
        with open(argument.lp, "w") as f:
            write_max(f, products, maxi)
            write_others(f, products, B, values, m)
            if argument.int:
                messages = "int "
                for i in range(len(products)):
                    messages += products[i]
                    if i < len(products) - 1:
                        messages += ","
                messages += ";"
                f.write(messages)

    

if __name__ == '__main__':
    main()

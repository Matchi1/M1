import argparse
from subprocess import check_output
import sys

class GenericsFileParserForLP:

    def __init__(self, input, output) -> None:
        self.input = input
        self.output = output
        self.nbFormule = 0
        self.nbVariable = 0
        self.result = []
        self.data = []
        self.opt = .0        # Value of objective function
        self.values = []     # Variables actual values

    def setParam(self, optionTab) -> None:
        self.nbFormule = int(optionTab[0])
        self.nbVariable = int(optionTab[1])

    def setSolution(self, resultTab) -> None:
        self.result = resultTab

    def getVariableCoeff(self) -> list:
        return [info[1:-1] for info in self.data]

    def getLabels(self) -> list:
        return [info[0] for info in self.data]

    def parse_file(self) -> None:
        lines = self.input.read().splitlines()
        self.setParam(lines[0].split(" "))
        self.setSolution(lines[1].split(" "))
        for i in range(2, len(lines)):
            self.data.append(lines[i].split(" "))

    def generate_lp(self) -> None:
        self.generate_max()

    def generate_max(self) -> None:
        self.output.write("max: ")
        add = ""
        for i in range(self.nbVariable):
            self.output.write(add)
            add = "+"
            self.output.write("" + self.data[i][-1] + self.data[i][0])
        self.output.write(";\n")

    def generate_equation(self) -> None:
        labels = self.getLabels()
        coeffs = self.getVariableCoeff()
        for i in range(self.nbFormule):
            equation = [coeffs[j][i] + labels[j] for j in range(self.nbVariable)]
            message = "+".join(equation) + " <= " + self.result[i] + ";\n"
            self.output.write(message)

    def generate_int(self) -> None:
        labels = self.getLabels()
        message = "int " + ",".join(labels) + ";\n"
        self.output.write(message)

    def set_optimum(self, output) -> None:
        line = output[0].split()
        self.opt = float(line[-1])

    def set_variables_value(self, output, arguments) -> None:
        for line in output[1:]:
            data = line.split()
            if arguments.int:
                self.values.append((data[0], int(data[1])))
            else:
                self.values.append((data[0], float(data[1])))
            

    def run_lp_solve(self, arguments) -> None:
        output = check_output(['lp_solve',
            'output.lp']).decode('utf-8').split('\n')
        output = list(filter(None, output))
        output.pop(1)
        self.set_optimum(output)
        self.set_variables_value(output, arguments)

    def display_result(self) -> str:
        message = "opt = {}\n".format(self.opt)
        for key, value in self.values:
            message += "{} = {}\n".format(key, value)
        return message

    def close(self) -> None:
        self.input.close()
        self.output.close()

    def __str__(self) -> str:
        return """
        Nb equation {}
        Nb variable {}
        Data solution = {}
        Data content = {}
        """.format(self.nbFormule, self.nbVariable, self.result, self.data)

def parse_arguments():
    parser = argparse.ArgumentParser(description='Generate a LP file from template')
    parser.add_argument('input', nargs='+', type=argparse.FileType('r'), default=sys.stdin, help="Input for your template")
    parser.add_argument('output', nargs='+', type=argparse.FileType('w'), default=sys.stdout, help="Output LP file generated from your template")
    parser.add_argument('-int', action='store_true')
    return parser.parse_args()


if __name__ == '__main__':

    arguments = parse_arguments()
    parser = GenericsFileParserForLP(arguments.input[0], arguments.output[0])

    parser.parse_file()
    print(parser)
    parser.generate_lp()
    parser.generate_equation()
    if arguments.int:
        parser.generate_int()
    parser.close()
    parser.run_lp_solve(arguments)
    print(parser.display_result())
    

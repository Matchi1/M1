#!/bin/python3

import pickle
from urllib import request

def display(char, number):
    for _ in range(number):
        print(char, end='')

with request.urlopen('http://www.pythonchallenge.com/pc/def/banner.p') as f:
    data = pickle.load(f)
    for line in data:
        for values in line:
            display(values[0], values[1])
        print()

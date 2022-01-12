#!/bin/python3

import itertools

product = itertools.product('ab', repeat=3)
product4 = itertools.product('abc', repeat=3)
product1 = itertools.product('a', 'b')
product2 = itertools.product('ab', 'cd')
print('product')
for i in product:
    print(i)
print('product1')
for i in product1:
    print(i)
print('product2')
for i in product2:
    print(i)
print('product4')
for i in product4:
    print(i)

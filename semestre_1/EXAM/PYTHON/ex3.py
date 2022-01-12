#!/bin/python3

def treemap(f, T:list) -> list:
    result = []
    for elt in T:
        if isinstance(elt, int):
            result.append(f(elt))
        else:
            result.append(treemap(f, elt))
    return result

T = [[0,1],[[[2,3],[4,5]]], [[6,7,[8,9]]]]

def f(x:int):
    return x*x

print(treemap(f,T))

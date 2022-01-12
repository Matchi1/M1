#!/bin/python3

def g(ll:list):
    return list({value:value for value in ll}.values())
ll = [1,2,1,1,3,2,'toto',3,5,6,5,'toto',6,6,'bla',8,8,6,'bla',8,9,9,1,1,2]
print(g(ll))

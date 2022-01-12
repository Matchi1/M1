def f1(k, n):
    return sum([i**k for i in range(0, n)])

print(f1(2, 3))
print(f1(2, 4))

import math

def f2(n):
    return len(str(math.factorial(n)))

print(f2(10))

def f3(ll, d):
    return [elt for elt in ll if elt % d == 0]

def f4(A:set, B:set):
    return [(a, b) for a in A for b in B]

def f5(M):
    return [[M[i][j] for i in range(len(M))] for j in range(len(M[0]))]

def f6(L1, L2):
    return True in [True for val1 in L1 for val2 in L2 if val1 == val2]

def f7(ll):
    return max(set(ll), key=ll.count)

def f8(s, t):
    return [False for i in range(len(s)) if s[i] != t[i]].count(False)

def f9(ll):
    return sorted(sorted(ll), key=len)

# Exercice 1

[x%3 or x for x in range(13)]
42 if 1^1 else 666 and 'toto'

ll = [int(''.join(x), 2) for x in product(*['01' for i range(3)])]

1. 44
2. [0, 1, 2, 3, 1, 2, 6, 1, 2, 9, 1, 2, 12]
3. 'toto'

# Exercice 2

def overlap(L1, L2):
    return [True for element in L1 if element in L2] == []
    
# Exercice 3

def how_many_args(*args):
    return len(args)
    
# Exercice 4

from itertools import permutations

def anagrams(message):
    words = [''.join(w) + '\n' for w in permutations(message, len(message))]
    result = []
    file = open('dico.txt', 'r') 
    for w in words:
        if w in file:
            words.append(w)
    return result
    
# Exercice 5

def suivant(message):
    word = sorted(list(message), reverse=True)
    dico = {value:word.count(value) for value in word}
    return ''.join([str(value) + str(key) for key, value in dico.items()])
    
    
def cache(func):
    dico = {}
    def find(word):
        if word in dico:
            return dico[word]
        else:
            value = suivant(word)
            dico[word] = value
            return value
    return find
            

# Exercice 1

1. 36 * 37 = 1337
2. {1, 3, 5, 7, 11, 13, 17, 19, 23, 27, 29, 31} donc 12 * 36 = 432
3. inverse_mod(5, 37) = 15, x = 15y - 45 [37]
4.
class Affine37():
    def __init__(self, a, b):
        self.a = a
        self.b = b
        self.n = 37

    def encrypt(self, message):
        return str([chr((self.a * ord(x) + self.b) % self.n) for x in message])

    def decrypt(self, message):
        inv = inverse_mod(y, self.n)
        return str([chr((inv * (ord(y) + self.b)) % self.n) for y in message])

5. 0 (27) -> L (11); B (1) -> E (4)

27 = 11a + b
1 = 4a + b

26 = a

a = 26/7

a = 9

1 = 4 * 9 + b

b = 2

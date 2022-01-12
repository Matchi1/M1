#!/bin/python3

from urllib import request

# '8022'
# 82683

path = 'http://www.pythonchallenge.com/pc/def/linkedlist.php?nothing='
end = '8022'
pattern = '\<body\>.*\<\/body\>'

while (True):
    with request.urlopen(path + end) as f:
        content = f.read().decode('utf-8')
        data = content.split()
        end = data[-1]
        print(content)

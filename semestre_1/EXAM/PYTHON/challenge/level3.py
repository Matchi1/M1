#!/usr/bin/python3

from urllib import request
import re

with request.urlopen('http://www.pythonchallenge.com/pc/def/equality.html') as file:
    content = file.read()
    content = content.decode('utf-8')
    comment = re.findall("<!--(.*?)-->", content, re.DOTALL)
    comment = comment[-1]
    result = re.findall("[a-z][A-Z]{3}[a-z][A-Z]{3}[a-z]", comment, re.DOTALL)
    parse = [word[i] for word in result for i in range(len(word)) if i == 4]
    print(''.join(parse))

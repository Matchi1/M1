#!/usr/bin/python3

from urllib import request
import re

with request.urlopen('http://www.pythonchallenge.com/pc/def/ocr.html') as file:
    content = file.read()
    content = content.decode('utf-8')
    comment = re.findall("<!--(.*?)-->", content, re.DOTALL)
    comment = comment[-1]
    count = {}
    for char in comment:
        if char not in count:
            count[char] = 1
        else:
            count[char] += 1
    print(count)

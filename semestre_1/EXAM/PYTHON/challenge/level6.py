#!/bin/python3

import re
import zipfile

f = zipfile.ZipFile('channel/channel.zip')
num = '90052'
result = []
while(True):
    content = f.read(num + ".txt").decode("utf-8")
    print(content)
    match = re.search("Next nothing is (\d+)", content)
    if match == None:
        break
    num = match.group(1)
    result.append(num + '.txt')

for value in result:
    print(f.getinfo(value).comment.decode('utf-8'), end='')

# oxygen

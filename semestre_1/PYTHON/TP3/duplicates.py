import os
import sys
import hashlib
import argparse

def parse_argument():
    parser = argparse.ArgumentParser()
    parser.add_argument('-e', '--extension', type=str, default=None)
    parser.add_argument('-d', '--directory', default='.')
    parser.add_argument('-o', '--output', default='stdout')
    return parser.parse_args()

def file_is_valid(filename, extension):
    if extension == None:
        return True
    regex = '.' + extension
    if os.path.islink(filename):
        return False
    return regex == filename[-4:]

def list_files(directory_name, extension):
    l = list(os.walk(directory_name))
    paths = []
    for directory in l:
        dirname, dirnames, filenames = directory
        for f in filenames:
            path = os.path.join(dirname, f)
            if file_is_valid(path, extension):
                paths.append(path)
    return paths

def hash_file(filename):
    h = hashlib.sha1()
    with open(filename, "rb") as file:
        for line in file.readlines():
            h.update(line)
    return h.digest()

def hash_files(paths):
    path_hash = {}
    l_hash = []
    for p in paths:
        current_hash = hash_file(p)
        if current_hash not in l_hash:
            l_hash.append(current_hash)
            path_hash[current_hash] = [p]
        else:
            path_hash[current_hash].append(p)
    return path_hash

def display_files(path_hash):
    for key in path_hash:
        for f in path_hash[key]:
            if(len(path_hash[key]) > 1):
                print(f)
        print()
    

def main():
    args = parse_argument()
    paths = list_files(args.directory, args.extension)
    path_hash = hash_files(paths)
    trash = [key for key in path_hash if len(path_hash[key]) < 2]
    for t in trash:
        path_hash.pop(t)
    display_files(path_hash)

if __name__ == '__main__':
    main()

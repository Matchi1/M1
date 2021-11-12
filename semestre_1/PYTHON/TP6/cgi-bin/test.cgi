#!/usr/bin/env python3

import cgi
import cgitb
import fcntl

def main():
    cgitb.enable()

    print("Content-Type: text/html")    # HTML is following
    print()                             # blank line, end of headers

    form = cgi.FieldStorage()
    fields = ["surname", "name", "email", "company", "telephone", "arrival", "departure"]
    for field in fields:
        if field not in form:
            print("<H1>Error</H1>")
            print("Please fill in the "+ field +" field.")
            return
        print("<p>" + field + " : " + form[field].value)

    lockfile = open("csv.lck", "w")
    fcntl.flock(lockfile.fileno(), fcntl.LOCK_EX) # pour verrouiller
    with open("participants.csv", "a") as file:
        for field in fields:
            file.write(form[field].value)
            file.write("|")
    fcntl.flock(lockfile.fileno(), fcntl.LOCK_UN) # pour relacher
    lockfile.close()

if __name__=='__main__':
    main()

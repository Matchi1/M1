import xmlrpc.client

def main():
    with xmlrpc.client.ServerProxy("http://localhost:8000/") as proxy:
        print("{}".format(proxy.hello()))

if __name__=='__main__':
    main()

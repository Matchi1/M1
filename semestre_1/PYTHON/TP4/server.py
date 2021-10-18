from xmlrpc.server import SimpleXMLRPCServer

def hello():
    return "hello"

def main():
    server = SimpleXMLRPCServer(("localhost", 8000))
    print("Listening on port 8000...")
    server.register_function(hello, "hello")
    server.serve_forever()

if __name__=='__main__':
    main()

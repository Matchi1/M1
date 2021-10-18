from flask import Flask

app = Flask(__name__)

@app.route("/")
def hello_world():
    return "<p>Hello, World!</p>"

@app.route("/haddock")
def haddock():
    return "<p>Hello, World!</p>"

@app.route("/jurons/<int:value>")
def multi_jurons(value):
    jurons = ''
    for _ in range(value):
        jurons += 'hello world<br>'
    return jurons

@app.route("/portrait")
def portrait():
    return "<img src=http://monge.univ-mlv.fr:8889/static/haddock.jpg>"


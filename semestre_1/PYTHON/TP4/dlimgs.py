import argparse
import os
from html.parser import HTMLParser
from urllib.parse import unquote, urlsplit, urlunsplit, urlparse, urljoin
from urllib.request import urlopen, urlretrieve

class MyHTMLParser(HTMLParser):
    imgs = []
    def __init__(self):
        HTMLParser.__init__(self)
        self.imgs = []

    def handle_starttag(self, tag, attrs):
        if (tag == 'img'):
            self.imgs.append(attrs[0])

def parse_argument():
    parser = argparse.ArgumentParser()
    parser.add_argument('url', metavar='URL', type=str, default=None, help='url of a website')
    return parser.parse_args()

def find_images(page):
    paths = []
    html_parser = MyHTMLParser()
    html_parser.feed(page)
    for img in html_parser.imgs:
        paths.append(img[1])
    return paths

def download_image(url, paths):
    parsed_url = urlparse(url)
    full_paths = [urljoin(url, path) for path in paths]
    try:
        os.mkdir("imgs")
    except FileExistsError:
        print("directory already exist")
    for path in full_paths:
        filename = urlsplit(path).path.split('/')[-1]
        urlretrieve(path, "imgs/" + filename)

def main():
    arg_parser = parse_argument()
    url = arg_parser.url
    page = urlopen(url).read().decode('utf-8')
    images = find_images(page)
    download_image(url, images)

if __name__ == '__main__':
    main()

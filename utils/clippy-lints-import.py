#!/usr/local/bin/python3

import urllib.request, json
from bs4 import BeautifulSoup

clippy_lints_url = "https://rust-lang.github.io/rust-clippy/master/index.html"


def get_name_from_docstring(docs):
    paragraph = docs.find('p')
    for code_tag in paragraph.find_all('code'):
        code_tag.replace_with(f"`{code_tag.text}`")
    docstring = paragraph.get_text()
    s = docstring.replace('\n',' ')
    s = s[:100] if len(s) > 101 else s
    return s.strip()



with urllib.request.urlopen(clippy_lints_url) as url:
    html = BeautifulSoup(url.read().decode(), 'html.parser')
    articles = html.find_all('article')
    
rules = []

for rule in articles:
    deprecated = rule.find(class_='label-group-deprecated')
    if (deprecated):
        continue
    id = rule.find(class_='panel-title-name')['id'].replace('lint-','')
    key = "clippy::" + id
    name = get_name_from_docstring(rule.find(class_='lint-docs'))
    url="https://rust-lang.github.io/rust-clippy/master/index.html#"+id

    sonar_rule={}
    sonar_rule["key"] = key
    sonar_rule["name"] = name
    sonar_rule["url"] = url
    rules.append(sonar_rule)

print(json.dumps(rules))
#!/usr/local/bin/python3

import urllib.request, json
clippy_lints_url = "https://rust-lang.github.io/rust-clippy/master/lints.json"
with urllib.request.urlopen(clippy_lints_url) as url:
    data = json.loads(url.read().decode())
    
rules = []
max_name_length = 100
for lint in data:

   if lint["group"] == "deprecated":
       continue

   id =  lint["id"]
   key="clippy::" + id
   name=lint["docs"]["What it does"][:max_name_length]
   url="https://rust-lang.github.io/rust-clippy/master/index.html#"+id
   sonar_rule={}
   sonar_rule["key"] = key
   sonar_rule["name"] = name
   sonar_rule["url"] = url
   rules.append(sonar_rule)

print(json.dumps(rules))

#!/usr/bin/env python3
#  -*- coding: UTF-8 -*-

# python3 nacos_config.py -dev

import http.client
import sys
import getopt as opts
import urllib.parse
import glob


def get_params() -> dict:
    params = {
        '-h': '127.0.0.1',
        '-p': '8848',
        '-t': '',  # namespace
        '-g': 'DEFAULT_GROUP',
        '-u': '',
        '-w': ''
    }
    inputs, args = opts.getopt(sys.argv[1:], shortopts='h:p:t:g:u:w:')
    for k, v in inputs:
        params[k] = v
    return params


def error_exit():
    print('python nacos-config.py [-h host] [-p port] [-t tenant] [-g group] [-u username] [-w password]')
    exit()


headers = {
    'content-type': "application/x-www-form-urlencoded"
}

hasError = False

params = get_params()

url_prefix = f"{params['-h']}:{params['-p']}"
tenant = params['-t']
username = params['-u']
password = params['-w']
group = params['-g']
url_postfix_base = f'/nacos/v1/cs/configs?group={group}&tenant={tenant}'
print(url_postfix_base)
if username != '' and password != '':
    url_postfix_base += f'&username={username}&password={password}'

if url_prefix == ':':
    error_exit()

for file in glob.glob('../resources/*'):
    with open(file) as f:
        pair = [file[file.rfind('/') + 1:], f.read()]
    format_name = file[file.rfind(".") + 1:]
    url_postfix = url_postfix_base + f'&type={format_name}&dataId={urllib.parse.quote(str(pair[0]))}&content={urllib.parse.quote(str(pair[1])).strip()}'
    conn = http.client.HTTPConnection(url_prefix)
    conn.request("POST", url_postfix, headers=headers)
    res = conn.getresponse()
    data = res.read().decode("utf-8")
    if data != "true":
        hasError = True
    print(f"{pair[0]} {data if hasError else 'success'}")

if hasError:
    print("init nacos config fail.")
else:
    print("init nacos config finished")

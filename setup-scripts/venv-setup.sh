#!/usr/bin/env bash

SCRIPTS_DIR=$( cd -- "$( dirname -- "${BASH_SOURCE[0]}" )" &> /dev/null && pwd )

cd $SCRIPTS_DIR/..

if [ -e py2venv4mcp ] ; then
    echo venv already exist
else
    echo generating venv
    virtualenv -p /usr/bin/python2 py2venv4mcp 
    exit
fi

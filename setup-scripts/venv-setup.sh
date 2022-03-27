#!/bin/bash

SCRIPTS_DIR=$(dirname "$0")

cd $SCRIPTS_DIR/..

if [ -e py2venv4mcp ] ; then
    echo venv already exist
else
    echo generating venv
    virtualenv -p /usr/bin/python2 py2venv4mcp 
    exit
fi

#!/bin/bash

MCP_URL=http://www.modcoderpack.com/files/mcp918.zip
SCRIPTS_DIR=$(dirname "$0")

cd $SCRIPTS_DIR/..

if ! sha256sum -c <(echo "c936dffb3007110b24538da5f334c28ec83c6787a56cc4c63fd840cdff306eb0 mcp918.zip")
then
    echo downloading mcp@$MCP_URL
    curl $MCP_URL -o mcp918.zip
fi


if ! sha256sum -c <(echo "c936dffb3007110b24538da5f334c28ec83c6787a56cc4c63fd840cdff306eb0 mcp918.zip")
then
    rm mcp918.zip
    echo "mcp seemes to be broken, exiting"
    exit 1
fi


echo decompressing mcp
unzip -o mcp918.zip -d mcp918

cd mcp918

git init .
git add .
git commit -m init
git apply ../mcp-patches/*

git add .
git commit -m "applied mcp patches"


cd ../assets/
unzip ../mcp918/jars/versions/1.8.8/1.8.8.jar -d minecraft-unpack

#!/usr/bin/env bash

MCP_URL=http://www.modcoderpack.com/files/mcp918.zip
OPTIFINE_URL=https://raw.githubusercontent.com/Hexeption/Optifine-SRC/master/Optifine%20SRC%20Version%20%5B1.8.8%20HD%20U%20H8%5D.zip
SCRIPTS_DIR=$( cd -- "$( dirname -- "${BASH_SOURCE[0]}" )" &> /dev/null && pwd )

cd $SCRIPTS_DIR/..

function download {
    if ! sha256sum -c <(echo "$3 $2")
    then
        echo downloading $2@$1
        curl $1 -o $2
    fi
    
    if ! sha256sum -c <(echo "$3 $2")
    then
        rm $2
        echo "$2 seemes to be broken, exiting"
        exit 1
    fi
}

download "$MCP_URL" "mcp918.zip" "c936dffb3007110b24538da5f334c28ec83c6787a56cc4c63fd840cdff306eb0"

download "$OPTIFINE_URL" "optifine-src.zip" "d8de2766db37093b9ca7a373af84497873029b26fea6024fd28e7708383da578"


echo decompressing mcp
unzip -o mcp918.zip -d mcp918

cd mcp918

git init .
git add .
git commit -m init

git apply ../mcp-patches/*
git add .
git commit -m "applied mcp patches"



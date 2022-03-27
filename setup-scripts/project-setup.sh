#!/bin/bash

# This currently assumes that you have already launched 1.8.8
SCRIPTS_DIR=$(dirname "$0")

bash $SCRIPTS_DIR/mcp-setup.sh
bash $SCRIPTS_DIR/venv-setup.sh

cd $SCRIPTS_DIR/../mcp918/
. ../py2venv4mcp/bin/activate

bash ./decompile.sh
git add .
git commit -m "decompiled minecraft"

unzip -o ../optifine-src.zip -d src/minecraft/
git add .
git commit -m "extracted optifine"

cd ../assets/
unzip -o ../mcp918/jars/versions/1.8.8/1.8.8.jar -d minecraft-unpack

cd ../mcp918
for patch in ../patches/*
do
    echo applying $patch
    git apply $patch
    git add .
    git commit -m "$(basename $patch .patch)"
done


#!/bin/bash

MC_DIR=${1:-$HOME/.minecraft}
DEST_DIR=$MC_DIR/versions/unnamed/
SCRIPTS_DIR=$(dirname "$0")

cd $SCRIPTS_DIR/..

mkdir -p $DEST_DIR
cp idea-project/out/artifacts/mcp918_jar/unnamed.jar $DEST_DIR
cp assets/unnamed.json $DEST_DIR

#!/bin/bash

COMPILE_DIR="./out/production/java-openflow/"

mkdir -p $COMPILE_DIR
javac -d $COMPILE_DIR -cp $COMPILE_DIR $(find ./src/ -name "*.java")

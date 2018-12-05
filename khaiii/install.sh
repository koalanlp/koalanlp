#!/usr/bin/env bash

WD=$(pwd)
if [ ! -d "$HOME/khaiii-orig" ]
then
    cd $HOME
    git clone https://github.com/kakao/khaiii.git khaiii-orig
fi

cd khaiii-orig
pip install -r requirements.txt

if [ ! -d "build" ]
then
    mkdir build
    cd build
    cmake ..
fi

if [ -f "Makefile" ]
then
    make all
    make resource
else
    echo "Makefile is not present!"
    exit 1
fi

export LD_LIBRARY_PATH=$LD_LIBRARY_PATH:$HOME/khaiii-orig/build/lib
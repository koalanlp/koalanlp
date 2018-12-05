#!/usr/bin/env bash

WD=$(pwd)
if [ ! -d "$HOME/khaiii" ]
then
    cd $HOME
    git clone https://github.com/kakao/khaiii.git
    cd khaiii
    pip install -r requirements.txt
fi

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

export LD_LIBRARY_PATH=$LD_LIBRARY_PATH:$HOME/khaiii/build/lib
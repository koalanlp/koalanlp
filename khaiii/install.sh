#!/usr/bin/env bash

WD=$(pwd)
if [ ! -d "$HOME/khaiii-orig" ]
then
    cd $HOME
    git clone https://github.com/kakao/khaiii.git khaiii-orig
fi

cd khaiii-orig
pip install -r requirements.txt

PYVER=$(python3 --version)
echo $PYVER

if [ ! -d "build" ]
then
    mkdir build
    cd build
    cmake ..
fi

if [ ! -f "lib/libkhaiii.so" ]
then
    make clean
    make all
else
    echo libkhaiii.so already exists.
fi

if [ ! -f "share/khaiii/restore.key" ]
then
    make resource
else
    echo resource files exist.
fi

export LD_LIBRARY_PATH=$LD_LIBRARY_PATH:$HOME/khaiii-orig/build/lib
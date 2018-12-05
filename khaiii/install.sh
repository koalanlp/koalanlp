#!/usr/bin/env bash

WD=$(pwd)
if [ ! -d "$HOME/khaiii-orig" ]
then
    cd $HOME
    git clone https://github.com/kakao/khaiii.git khaiii-orig
fi

cd khaiii-orig
wget https://bootstrap.pypa.io/get-pip.py
python3.6 get-pip.py --user
python3.6 -m pip install --user -r requirements.txt

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
    sudo mv /usr/bin/python3 /usr/bin/python3-old
    sudo ln -s /usr/bin/python3.6 /usr/bin/python3
    make resource
    sudo rm /usr/bin/python3
    sudo mv /usr/bin/python3-old /usr/bin/python3
else
    echo resource files exist.
fi

export LD_LIBRARY_PATH=$LD_LIBRARY_PATH:$HOME/khaiii-orig/build/lib
#!/usr/bin/env bash

WD=$(pwd)
if [ ! -d "$HOME/khaiii-orig" ]
then
    cd $HOME
    git clone https://github.com/kakao/khaiii.git khaiii-orig
    echo -e "\033[32mClone finished!\033[0m"
fi

cd khaiii-orig
wget https://bootstrap.pypa.io/get-pip.py
python3.6 get-pip.py --user
python3.6 -m pip install --user -r requirements.txt
echo -e "\033[32mPython3.6 installation finished\033[0m"

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
    echo -e "\033[32mBuild finished!\033[0m"
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

    echo -e "\033[32mResource build finished!\033[0m"
else
    echo resource files exist.
fi
#!/usr/bin/env bash

### Khaiii 저장소 clone
if [ ! -d "$HOME/khaiii-orig/.git" ]
then
    ### Clone to ~/khaiii-orig/
    cd $HOME
    git clone https://github.com/kakao/khaiii.git khaiii-orig
    cd khaiii-orig
    echo "\033[34mClone finished!\033[0m"
else
    ### Travis CI에 이미 Caching된 데이터가 있다면, pull
    cd khaiii-orig
    git pull origin master
    echo "\033[34mPull finished!\033[0m"
fi

KHAIII_LATEST=$(git tag -l | tail -n 1)
git checkout -f tags/$KHAIII_LATEST

### Python 3.6 Pypi install
wget https://bootstrap.pypa.io/get-pip.py
python3 get-pip.py --user
python3 -m pip install --user -r requirements.txt
echo "\033[34mPython3.6 installation finished\033[0m"

### Note original python version for checking.
PYVER=$(python3 --version)
echo $PYVER

### Make build files
if [ ! -d "build" ]
then
    mkdir build
    cd build
    cmake ..
fi

### Make shared object file
if [ ! -f "lib/libkhaiii.so.${KHAIII_LATEST}" ]
then
    make clean
    make all
    echo "\033[34mBuild finished!\033[0m"

    ### Make resources
    if [ ! -f "share/khaiii/restore.key" ]
    then
        ### Build resource
        make resource

        echo "\033[34mResource build finished!\033[0m"
    else
        echo resource files exist.
    fi
else
    echo libkhaiii.so.${KHAIII_LATEST} already exists.
fi

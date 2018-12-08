#!/usr/bin/env bash

### Khaiii 저장소 clone
if [ ! -d "$HOME/khaiii-orig/.git" ]
then
    ### Clone to ~/khaiii-orig/
    cd $HOME
    git clone https://github.com/kakao/khaiii.git khaiii-orig
    echo "\033[34mClone finished!\033[0m"
else
    ### Travis CI에 이미 Caching된 데이터가 있다면, pull
    git pull origin master
    echo "\033[34mPull finished!\033[0m"
fi

cd khaiii-orig

### Python 3.6 Pypi install
wget https://bootstrap.pypa.io/get-pip.py
python3.6 get-pip.py --user
python3.6 -m pip install --user -r requirements.txt
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
if [ ! -f "lib/libkhaiii.so" ]
then
    make clean
    make all
    echo "\033[34mBuild finished!\033[0m"
else
    echo libkhaiii.so already exists.
fi

### Make resources
if [ ! -f "share/khaiii/restore.key" ]
then
    ### Temporarily link python3 to python3.6
    sudo mv /usr/bin/python3 /usr/bin/python3-old
    sudo ln -s /usr/bin/python3.6 /usr/bin/python3
    ### Build resource
    make resource
    ### Restore link: python3 to python3.5
    sudo rm /usr/bin/python3
    sudo mv /usr/bin/python3-old /usr/bin/python3

    echo "\033[34mResource build finished!\033[0m"
else
    echo resource files exist.
fi

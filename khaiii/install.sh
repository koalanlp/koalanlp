#!/usr/bin/env bash

if [ $TRAVIS_OS_NAME == 'windows' ]
then
    echo "Khaiii does not support Windows architecture."
    exit 0
fi

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
    cd $HOME/khaiii-orig
    git pull origin master
    echo "\033[34mPull finished!\033[0m"
fi

KHAIII_LATEST=$(git tag -l | tail -n 1)
git checkout -f tags/$KHAIII_LATEST

### Make build files
if [ ! -d "build" ]
then
    mkdir build
fi

# OS Release check (khaiii/khaiii#103)
if [ $TRAVIS_OS_NAME == 'linux' ]
then
    RELEASE=`lsb_release -cs`
else
    RELEASE=none
fi

cd build
if [ $RELEASE == 'focal' ]
then
    cmake -E env CXXFLAGS="-w" cmake ..
else
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


if [ "$TRAVIS_OS_NAME" != "linux" ]
then
    make install
fi
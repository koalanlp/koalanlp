#!/usr/bin/env bash
# Get to the Travis build directory, configure git and clone the repo
WD=$(pwd)
MSG=$TRAVIS_COMMIT_MESSAGE
TAG=`cat gradle.properties | grep "version=" | cut -d= -f2`

git config --global user.email "travis@travis-ci.org"
git config --global user.name "travis-ci"

gradle jacocoTestReport
bash <(curl -s https://codecov.io/bash)

#    if [[ $TRAVIS_EVENT_TYPE == "push" ]]; then
#        # GENERATE DOC
#        sbt ++$SCALAVER unidoc
#
#        # GENERATE COMPARISON
#        sbt -J-Xmx3g ++$SCALAVER "samples/runMain kr.bydelta.koala.wiki.ComparisonGenerator ./4.1.-임의-결과-비교.md $SCALAVER"
#
#        # CLONE GH-PAGES
#        cd $HOME
#        git clone --quiet --branch=gh-pages git@github.com:koalanlp/KoalaNLP-core gh-pages > /dev/null
#
#        # COPY & PUSH
#        cd gh-pages
#        git rm -rf ./api/*
#        mkdir ./api
#
#        mv $WD/target/scala-$VER/unidoc $HOME/gh-pages/api/scala
#        mv $WD/target/javaunidoc $HOME/gh-pages/api/java
#        mv $WD/README.md $HOME/gh-pages/index.md
#
#        git add -f ./api
#        git add -f ./index.md
#
#        git commit -m "Lastest javadoc on successful travis build $TRAVIS_BUILD_NUMBER (RELEASE $TAG) auto-pushed to gh-pages"
#        git push -fq origin gh-pages > /dev/null
#
#        # CLONE WIKI
#        cd $HOME
#        git clone --quiet git@github.com:koalanlp/KoalaNLP-core.wiki.git wiki > /dev/null
#
#        # COPY & PUSH
#        cd wiki
#        git rm -f "./4.1.-임의-결과-비교.md"
#        mv "$WD/4.1.-임의-결과-비교.md" ./
#        git add -f "./4.1.-임의-결과-비교.md"
#
#        git commit -m "Lastest comparison in successful travis build $TRAVIS_BUILD_NUMBER (RELEASE $TAG) auto-pushed to wiki"
#        git push -fq origin master > /dev/null
#    fi

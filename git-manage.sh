# Get to the Travis build directory, configure git and clone the repo
WD=$(pwd)
VER=`echo $TRAVIS_SCALA_VERSION | cut -d. -f1,2`
MSG=$TRAVIS_COMMIT_MESSAGE
TAG=`cat build.sbt | grep "val VERSION" | cut -d\" -f2`

git config --global user.email "travis@travis-ci.org"
git config --global user.name "travis-ci"

if [[ $TRAVIS_EVENT_TYPE == "cron" ]]; then
    cp sonatype.sbt ~/.sbt/0.13/

    # ADD SNAPSHOT VARIABLE
    cat build.sbt | sed -e 's/val VERSION\s*=\s*"\(.*\)"/val VERSION = "\1-SNAPSHOT"/g' | tee build.sbt > \dev\null
    sbt ++$TRAVIS_SCALA_VERSION publish

elif [[ $TRAVIS_EVENT_TYPE == "push" ]]; then
    if [[ $MSG == *"RELEASE"* ]]; then
        # SET RELEASE TAG
        git tag v$TAG
        git push --tags


        # GENERATE DOC
        sbt ++$1 unidoc

        # CLONE GH-PAGES
        cd $HOME
        git clone --quiet --branch=gh-pages https://${GH_TOKEN}@github.com/nearbydelta/KoalaNLP gh-pages > /dev/null

        # COPY & PUSH
        cd gh-pages
        git rm -rf ./api/*
        mkdir ./api

        mv $WD/target/scala-$VER/unidoc $HOME/gh-pages/api/scala
        mv $WD/target/javaunidoc $HOME/gh-pages/api/java
        mv $WD/README.md $HOME/gh-pages/index.md

        git add -f ./api
        git add -f ./index.md

        git commit -m "Lastest javadoc on successful travis build $TRAVIS_BUILD_NUMBER auto-pushed to gh-pages"
        git push -fq origin gh-pages > /dev/null
    fi
fi
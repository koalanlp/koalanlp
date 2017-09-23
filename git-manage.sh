# Get to the Travis build directory, configure git and clone the repo
WD=$(pwd)
SCALAVER=`cat build.sbt | grep "scalaVersion := " | cut -d\" -f2`
VER=`echo $SCALAVER | cut -d. -f1,2`
MSG=$TRAVIS_COMMIT_MESSAGE
TAG=`cat build.sbt | grep "val VERSION" | cut -d\" -f2`

git config --global user.email "travis@travis-ci.org"
git config --global user.name "travis-ci"

if [[ $TRAVIS_SCALA_VERSION == $SCALAVER ]]; then
    sbt -J-Xmx3g ++$TRAVIS_SCALA_VERSION coverageReport coverageAggregate
    bash <(curl -s https://codecov.io/bash)

    if [[ $TRAVIS_EVENT_TYPE == "push" ]]; then
        # GENERATE DOC
        sbt ++$SCALAVER unidoc

        # GENERATE COMPARISON
        sbt -J-Xmx3g ++$SCALAVER "samples/runMain kr.bydelta.koala.wiki.ComparisonGenerator ./4.1.-임의-결과-비교.md $SCALAVER"

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

        git commit -m "Lastest javadoc on successful travis build $TRAVIS_BUILD_NUMBER (RELEASE $TAG) auto-pushed to gh-pages"
        git push -fq origin gh-pages > /dev/null

        # CLONE WIKI
        cd $HOME
        git clone --quiet https://${GH_TOKEN}@github.com/nearbydelta/KoalaNLP.wiki.git wiki > /dev/null

        # COPY & PUSH
        cd wiki
        git rm -f "./4.1.-임의-결과-비교.md"
        mv "$WD/4.1.-임의-결과-비교.md" ./
        git add -f "./4.1.-임의-결과-비교.md"

        git commit -m "Lastest comparison in successful travis build $TRAVIS_BUILD_NUMBER (RELEASE $TAG) auto-pushed to wiki"
        git push -fq origin master > /dev/null
    fi
fi

cd $WD
cp sonatype.sbt ~/.sbt/0.13/
sbt ++$TRAVIS_SCALA_VERSION publish
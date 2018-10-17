#!/usr/bin/env bash
# Get to the Travis build directory, configure git and clone the repo
WD=$(pwd)
MSG=$TRAVIS_COMMIT_MESSAGE
TAG=`cat gradle.properties | grep "version=" | cut -d= -f2`

git config --global user.email "travis@travis-ci.org"
git config --global user.name "travis-ci"

./gradlew jacocoTestReport
bash <(curl -s https://codecov.io/bash)

if [[ $TRAVIS_EVENT_TYPE == "push" ]]; then
    git rm -rf ./docs

    ./gradlew dokka

    mv build/javadoc/ ./docs/
    git add -f ./docs
    git commit -m "Lastest javadoc on successful travis build $TRAVIS_BUILD_NUMBER (RELEASE $TAG) auto-pushed to gh-pages"

    git push -fq origin $TRAVIS_BRANCH > /dev/null
fi

# Upload snapshot build
./gradlew uploadArchives closeAndReleaseRepository -PossrhUsername="${SONATYPE_USERNAME}" -PossrhPassword="${SONATYPE_PASSWORD}"
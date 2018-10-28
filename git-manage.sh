#!/usr/bin/env bash
# Get to the Travis build directory, configure git and clone the repo
./gradlew jacocoTestReport aggregateTestReport
bash <(curl -s https://codecov.io/bash)

TAG=`cat gradle.properties | grep "version=" | cut -d= -f2`
case $TAG in
    *SNAPSHOT)
        # Upload snapshot build
        ./gradlew uploadArchives -PossrhUsername="${SONATYPE_USERNAME}" -PossrhPassword="${SONATYPE_PASSWORD}"
        ;;
    *)
        ;;
esac
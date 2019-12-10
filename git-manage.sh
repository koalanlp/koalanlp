#!/usr/bin/env bash
if [ $TRAVIS_OS_NAME != 'linux' ]
then
    exit 0
fi

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
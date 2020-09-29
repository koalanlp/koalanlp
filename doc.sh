#!/usr/bin/env bash

JAR_VER=$(cat gradle.properties | grep "version=" | cut -d= -f2 | cut -d- -f1)
DATE=`date +%D`

mv docs/api/style.css docs/doc-style.css
rm -r docs/api/*
./gradlew clean dokka
rm docs/api/style.css
mv docs/doc-style.css docs/api/style.css
git add docs/api
git commit -a -m "Documentation of v$JAR_VER at $DATE"
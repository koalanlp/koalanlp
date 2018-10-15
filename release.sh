#!/usr/bin/env bash

JAR_VER=$(cat gradle.properties | grep "version=" | cut -d= -f2 | cut -d- -f1)
JAR_VER_MAJOR=$(echo $JAR_VER | cut -d. -f1)
JAR_VER_MINOR=$(echo $JAR_VER | cut -d. -f2)
JAR_VER_INCRM=$(echo $JAR_VER | cut -d. -f3)
JAR_VER_CURRENT=$JAR_VER_MAJOR.$JAR_VER_MINOR.$JAR_VER_INCRM
JAR_VER_NEXT=$JAR_VER_MAJOR.$JAR_VER_MINOR.$(($JAR_VER_INCRM + 1))

# reset version code
echo BUILD $JAR_VER_CURRENT

#echo BUILD EUNJEON DICT
#run_sbt ++2.12.0 eunjeon/run

cat gradle.properties | sed -e 's/version=\s*.*/version='$JAR_VER_CURRENT'/g' > gradle.properties.new
rm gradle.properties
mv gradle.properties.new gradle.properties
git add gradle.properties
git commit -m "RELEASE v$JAR_VER_CURRENT"
git tag v$JAR_VER_CURRENT

gradle clean jar dokkarJar sourceJar uploadArchives

echo SET TO $JAR_VER_NEXT
cat gradle.properties | sed -e 's/version=\s*.*/version='$JAR_VER_CURRENT'-SNAPSHOT/g' > gradle.properties.new
rm gradle.properties
mv gradle.properties.new gradle.properties

git add gradle.properties
git commit -a -m "inital commit of v$JAR_VER_NEXT"
git push origin master
git push --tags
#!/usr/bin/env bash

SCALA_VERS=$(cat build.sbt | grep crossScalaVersions | cut -d\" -f2,4 --output-delim=$' ')

JAR_VER=$(cat build.sbt | grep "val VERSION" | cut -d\" -f2 | cut -d- -f1)
JAR_VER_MAJOR=$(echo $JAR_VER | cut -d. -f1)
JAR_VER_MINOR=$(echo $JAR_VER | cut -d. -f2)
JAR_VER_INCRM=$(echo $JAR_VER | cut -d. -f3)
JAR_VER_CURRENT=$JAR_VER_MAJOR.$JAR_VER_MINOR.$JAR_VER_INCRM
JAR_VER_NEXT=$JAR_VER_MAJOR.$JAR_VER_MINOR.$(($JAR_VER_INCRM + 1))

SBT_PATH=java -jar ~/.IntelliJIdea2017.3/system/sbt/sbt-launch.jar

# reset version code
echo BUILD $JAR_VER_CURRENT

echo BUILD EUNJEON DICT
$SBT_PATH ++2.12.0 eunjeon/run
git add eunjeon/src/main/resources-2.12/*

cat build.sbt | sed -e 's/val VERSION\s*=\s*".*"/val VERSION = "'$JAR_VER_CURRENT'"/g' > build_new.sbt
rm build.sbt
mv build_new.sbt build.sbt
git add build.sbt
git commit -m "RELEASE v$JAR_VER_CURRENT"
git tag v$JAR_VER_CURRENT

for SCALA in $SCALA_VERS; do
    $SBT_PATH ++$SCALA publishSigned
done

echo SET TO $JAR_VER_NEXT
cat build.sbt | sed -e 's/val VERSION\s*=\s*".*"/val VERSION = "'$JAR_VER_NEXT'-SNAPSHOT"/g' > build_new.sbt
rm build.sbt
mv build_new.sbt build.sbt

git add build.sbt
git commit -a -m "inital commit of v$JAR_VER_NEXT"
git push origin master
git push --tags
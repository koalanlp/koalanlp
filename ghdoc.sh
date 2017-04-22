# Get to the Travis build directory, configure git and clone the repo
WD=$(pwd)
VER=`echo $1 | cut -d. -f1,2`

cd $HOME
git config --global user.email "travis@travis-ci.org"
git config --global user.name "travis-ci"
git clone --quiet --branch=gh-pages https://${GH_TOKEN}@github.com/nearbydelta/KoalaNLP gh-pages > /dev/null

# Commit and Push the Changes
cd gh-pages
git rm -rf ./api/*
mv $WD/target/scala-$VER/unidoc $HOME/gh-pages/api/scala
mv $WD/target/javaunidoc $HOME/gh-pages/api/java
mv $WD/README.md $HOME/gh-pages/index.md
git add -f ./api
git add -f ./index.md
git commit -m "Lastest javadoc on successful travis build $TRAVIS_BUILD_NUMBER auto-pushed to gh-pages"
git push -fq origin gh-pages > /dev/null
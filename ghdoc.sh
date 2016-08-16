# Get to the Travis build directory, configure git and clone the repo
WD=$(pwd)

cd $HOME
git config --global user.email "travis@travis-ci.org"
git config --global user.name "travis-ci"
git clone --quiet --branch=gh-pages https://${GH_TOKEN}@github.com/nearbydelta/KoalaNLP gh-pages > /dev/null

# Commit and Push the Changes
cd gh-pages
git rm -rf ./latest/api/*
cp -Rf $WD/target/scala-2.10/unidoc ./latest/api
git add -f ./latest/api
git commit -m "Lastest javadoc on successful travis build $TRAVIS_BUILD_NUMBER auto-pushed to gh-pages"
git push -fq origin gh-pages > /dev/null
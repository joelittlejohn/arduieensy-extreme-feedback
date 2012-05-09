#!/bin/sh

# best to stop if anything goes wrong
set -e

# get everything up-to-date, ready to release master
git checkout master && git pull

# lets do all our changes on a local release branch
git checkout -b release || git checkout release

# first we'll decide the new version numbers (with some sanity checks)
CURRENT_VERSION=$(mvn help:evaluate -Dexpression=project.version | grep -v INFO)
if [[ ! ($CURRENT_VERSION =~ .*\-SNAPSHOT) ]]; then
    echo "Current version ($CURRENT_VERSION) doesn't appear to be a snapshot, can't proceed with release"
    exit 1
fi
RELEASE_VERSION=$(echo $CURRENT_VERSION | sed 's/-SNAPSHOT//')
if [[ ! ($RELEASE_VERSION =~ [0-9]+\.[0-9]+\.[0-9]+) ]]; then
    echo "Something went wrong trying to generate the next release version, the string we got was: $RELEASE_VERSION"
    exit 2
fi
BUILD_NUMBER=$(echo $RELEASE_VERSION | sed -e 's/[0-9]*\.[0-9]*\.\([0-9]*\)/\1/')
if [[ ! ($BUILD_NUMBER =~ [0-9]+) ]]; then
    echo "Something went wrong trying to capture the current build number, the string we got was: $BUILD_NUMBER"
    exit 3
fi
NEXT_DEVELOPMENT_VERSION=$(echo $RELEASE_VERSION | grep -o '^[0-9]*\.[0-9]*.')$(expr $BUILD_NUMBER + 1)-SNAPSHOT
if [[ ! ($NEXT_DEVELOPMENT_VERSION =~ [0-9]+\.[0-9]+\.[0-9]+-SNAPSHOT) ]]; then
    echo "Something went wrong trying to generate the next development version, the string we got was: $NEXT_DEVELOPMENT_VERSION"
    exit 4
fi

# print some information about this release
echo CURRENT_VERSION: $CURRENT_VERSION
echo RELEASE_VERSION: $RELEASE_VERSION
echo NEXT_DEVELOPMENT_VERSION: $NEXT_DEVELOPMENT_VERSION

# now update to the release version and tag
mvn versions:set -DnewVersion=$RELEASE_VERSION -DgenerateBackupPoms=false \
    scm:checkin -Dmessage="[release] prepare release ($RELEASE_VERSION)" \
    scm:tag -Dtag="\${project.artifactId}-$RELEASE_VERSION" -DpushChanges=false

# perform the actual release
mvn deploy "$@"

# update to the next development version
mvn versions:set -DnewVersion=$NEXT_DEVELOPMENT_VERSION -DgenerateBackupPoms=false \
    scm:checkin -Dmessage="[release] prepare for next development iteration ($NEXT_DEVELOPMENT_VERSION)" -DpushChanges=false

# now lets switch back to master and pull in anything new
git checkout master && git pull origin master

# finally merge all our release changes into master and push
git merge release && git push origin master --tags

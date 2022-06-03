#!/bin/sh
dir >> log.txt
BASE_PWD="$PWD"
FLOWABLE_VERSION=6.5.0

echo "Initializing for version $FLOWABLE_VERSION"

mkdir -p $BASE_PWD/assets && rm -f $BASE_PWD/assets/*.original

echo "Building Flowable IDM"
cd ../../modules/flowable-ui-idm
mvn -T 1C clean install -DskipTests -Pdocker-deps
STATUS=$?
if [ $STATUS -eq 0 ]
then
    echo "Copying artifact"
	cp flowable-ui-idm-app/target/*.original $BASE_PWD/assets/
else
    echo "Error while building root pom. Halting."
fi

cd $BASE_PWD

echo "Building Flowable Modeler"
cd ../../modules/flowable-ui-modeler
mvn -T 1C clean install -DskipTests -Pdocker-deps
STATUS=$?
if [ $STATUS -eq 0 ]
then
    echo "Copying artifact"
	cp flowable-ui-modeler-app/target/*.original $BASE_PWD/assets/
else
    echo "Error while building root pom. Halting."
fi

cd $BASE_PWD

echo "Building Docker image for version: $FLOWABLE_VERSION"

docker build -t adito/flowable:2022.0.3 .

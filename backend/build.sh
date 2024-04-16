#!/bin/sh

cd ./qanda || exit
./gradlew clean build publishToMavenLocal
cd ..


cd ./ums || exit
./gradlew clean build
cd ..

cd ./qms || exit
./gradlew clean build
cd ..

cd ./ams || exit
./gradlew clean build
cd ..


docker-compose build
docker-compose up -d

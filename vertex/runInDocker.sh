#!/bin/bash
# before running this file, build the project first.
cores=1
if [ "$1" ];
then
  cores=$1
fi
set -x

docker run -it --cpus "$cores" -m 2g -v "${PWD}":/app --workdir /app -p 8080:8080 openjdk:19-jdk java -jar build/libs/vdemo-1.0.0-SNAPSHOT-fat.jar

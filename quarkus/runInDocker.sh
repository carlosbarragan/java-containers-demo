#!/bin/bash
# before running this file, build the project first.
cores=1
if [ "$1" ];
then
  cores=$1
fi
set -x
docker run -it --cpus "$cores" -m 2g -v ${PWD}:/app --workdir /app openjdk:19-jdk-alpine3.16 java -jar build/quarkus-app/quarkus-run.jar
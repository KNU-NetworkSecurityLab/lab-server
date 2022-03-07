#!/bin/sh

exec java -Djava.security.egd=file:/dev/./urandom -jar /docker-springboot.jar

mkdir -p /app/tmp
mkdir -p /app/logs

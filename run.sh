#!/bin/sh

mvn clean package
cd infra
docker compose up -d


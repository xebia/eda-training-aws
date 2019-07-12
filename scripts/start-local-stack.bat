#!/usr/bin/env bash

function start_localstack {
    pushd localstack
    echo "starting localstack in: `pwd`..."
    TMPDIR=/private$TMPDIR SERVICES="sns,sqs,kinesis,dynamodb,cloudwatch" DEFAULT_REGION=eu-west-1 docker-compose up -d
    echo "localstack started"
    popd
}

function install_localstack {
  echo "installing localstack..."
  git clone https://github.com/localstack/localstack.git
  echo "localstack installed under: `pwd`"

}

PUSHD ../..
if [ ! -d "localstack" ]; then
  install_localstack
fi
start_localstack
popd


#!/usr/bin/env bash

function start_localstack {
    pushd localstack
    echo "starting localstack in `pwd`..."
    TMPDIR=/private$TMPDIR SERVICES="sqs,sns,kinesis,dynamodb,cloudwatch,lambda,apigateway" DEFAULT_REGION=eu-west-1 LAMBDA_EXECUTOR=docker-reuse docker-compose up -d
    echo "localstack started"
    popd
}

function install_localstack {
  echo "installing localstack..."
  git clone https://github.com/localstack/localstack.git
  echo "localstack installed under: `pwd`"

}

pushd ../..
if [ ! -d "localstack" ]; then
  install_localstack
fi
start_localstack
popd



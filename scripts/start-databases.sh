#!/usr/bin/env bash

pushd ..
docker-compose up -d order-db inventory-db crm-db
popd


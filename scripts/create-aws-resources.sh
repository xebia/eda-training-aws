#!/bin/bash

AWS_REGION=eu-west-1

# Queue for orderPlaced events
aws sqs create-queue --queue-name orderPlaced --endpoint http://localhost:4576 --region $AWS_REGION

# SNS topic + SQS queues + subscriptions for OrderShipped
aws sns create-topic --name orderShipped --endpoint http://localhost:4575 --region $AWS_REGION

aws sqs create-queue --queue-name orderShippedEvent --endpoint http://localhost:4576 --region $AWS_REGION
aws sqs create-queue --queue-name orderShippedNotification --endpoint http://localhost:4576 --region $AWS_REGION

aws sns subscribe --topic-arn arn:aws:sns:${AWS_REGION}:123456789012:orderShipped --protocol sqs --notification-endpoint arn:aws:sqs:${AWS_REGION}:queue:orderShippedEvent --endpoint http://localhost:4575 --region $AWS_REGION
aws sns subscribe --topic-arn arn:aws:sns:${AWS_REGION}:123456789012:orderShipped --protocol sqs --notification-endpoint arn:aws:sqs:${AWS_REGION}:queue:orderShippedNotification --endpoint http://localhost:4575 --region $AWS_REGION

# Kinesis stream for replicating customers
aws kinesis create-stream --stream-name customerReplication --shard-count 2 --endpoint http://localhost:4568 --region $AWS_REGION

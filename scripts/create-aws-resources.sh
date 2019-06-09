#!/bin/bash

# Queue for OrderCreated events
aws sqs create-queue --queue-name orderCreated --endpoint http://localhost:4576

# SNS topic + SQS queues + subscriptions for OrderShipped
aws sns create-topic --name orderShipped --endpoint http://localhost:4575

aws sqs create-queue --queue-name orderShippedEvent --endpoint http://localhost:4576
aws sqs create-queue --queue-name orderShippedNotification --endpoint http://localhost:4576

aws sns subscribe --topic-arn arn:aws:sns:eu-west-1:123456789012:orderShipped --protocol sqs --notification-endpoint arn:aws:sqs:eu-west-1:queue:orderShippedEvent --endpoint http://localhost:4575
aws sns subscribe --topic-arn arn:aws:sns:eu-west-1:123456789012:orderShipped --protocol sqs --notification-endpoint arn:aws:sqs:eu-west-1:queue:orderShippedNotification --endpoint http://localhost:4575

# Kinesis stream for replicating customers
aws kinesis create-stream --stream-name customerReplication --shard-count 2 --endpoint http://localhost:4568

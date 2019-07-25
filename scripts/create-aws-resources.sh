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


#s3
#aws --endpoint-url=http://localhost:4572 s3 mb s3://auditLoggingBucket

#create bucket with read rights
aws s3api create-bucket --bucket auditLoggingBucket --grant-read public-read --endpoint-url=http://localhost:4572

#list buckets
aws --endpoint-url=http://localhost:4572 s3 ls

#aws s3api put-bucket-acl --acl public-read --bucket auditLoggingBucket --endpoint-url=http://localhost:4572

#list objects of bucket
#aws s3api list-objects --bucket auditLoggingBucket --endpoint-url=http://localhost:4572 --region eu-west-1

#get object from bucket
#aws s3api get-object --key "/c5826ef9-daa6-423c-a4f8-7c048f9513c0" --bucket auditLoggingBucket --endpoint-url=http://localhost:4572 --region eu-west-1  out.txt


#firehose
#create
aws firehose --endpoint http://localhost:4573 create-delivery-stream \
      --delivery-stream-name auditLoggingStream \
      --s3-destination-configuration file://./firehose.json

#describe
aws firehose --endpoint http://localhost:4573 describe-delivery-stream --delivery-stream-name auditLoggingStream

#put record
#aws firehose  --endpoint http://localhost:4573  put-record --delivery-stream-name auditLoggingStream --record='Data="{\"foo\":\"bar\"}"'

#delete
#aws firehose --endpoint http://localhost:4573 delete-delivery-stream --delivery-stream-name  auditLoggingStream







#!/bin/bash
# Create zipfile of src directory
TAG=$(date +%s)
REGION=eu-west-1
STAGE=production
LAMBDA_NAME=event-to-http-$TAG
GATEWAY_NAME=$LAMBDA_NAME-$TAG-gateway

# Build zipfile with vendored dependencies
pushd src
zip -r9 ../out/$LAMBDA_NAME.zip .
popd

pushd vendor
zip -r9 -g ../out/$LAMBDA_NAME.zip .
popd

# Deploy function to LocalStack
echo "Deploying function..."
aws lambda create-function --function-name $LAMBDA_NAME --runtime python3.6 --handler handler.handler --zip-file fileb://out/$LAMBDA_NAME.zip --role arn:aws:iam::123456:role/irrelevant --region $REGION --endpoint http://localhost:4574
LAMBDA_ARN=$(aws lambda list-functions --query "Functions[?FunctionName==\`$LAMBDA_NAME\`].FunctionArn" --output text --region $REGION --endpoint http://localhost:4574)

# Remove old event source mapping
echo "Removing old SQS subscription..."
EVENT_SOURCE_UUID=$(aws lambda list-event-source-mappings --query "EventSourceMappings[?EventSourceArn==\`arn:aws:sqs:elasticmq:000000000000:orderPlaced\`].UUID" --output text --region $REGION --endpoint http://localhost:4574)
aws lambda delete-event-source-mapping --uuid $EVENT_SOURCE_UUID --region $REGION --endpoint http://localhost:4574

# Bind function to `orderPlaced` SQS queue (assuming it was created)
echo "Creating new SQS subscription for lambda..."
aws lambda create-event-source-mapping \
    --event-source-arn arn:aws:sqs:elasticmq:000000000000:orderPlaced \
    --function-name $LAMBDA_NAME \
    --batch-size 1 \
    --endpoint http://localhost:4574

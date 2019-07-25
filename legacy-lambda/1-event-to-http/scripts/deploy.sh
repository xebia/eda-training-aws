#!/bin/bash
# Create zipfile of src directory
TAG=$(date +%s)
REGION=us-east-1
STAGE=production
LAMBDA_NAME=event-to-http-$TAG
GATEWAY_NAME=$LAMBDA_NAME-$TAG-gateway

pushd src
zip -r ../out/$LAMBDA_NAME.zip .
popd

# Deploy function to LocalStack
echo "Deploying function..."
aws lambda create-function --function-name $LAMBDA_NAME --runtime python3.6 --handler handler.handler --zip-file fileb://out/$LAMBDA_NAME.zip --role arn:aws:iam::123456:role/irrelevant --region $REGION --endpoint http://localhost:4574
LAMBDA_ARN=$(aws lambda list-functions --query "Functions[?FunctionName==\`$LAMBDA_NAME\`].FunctionArn" --output text --region $REGION --endpoint http://localhost:4574)

#!/bin/bash
# Create zipfile of src directory
TAG=$(date +%s)
REGION=us-east-1
STAGE=production
LAMBDA_NAME=http-to-event-$TAG
GATEWAY_NAME=$LAMBDA_NAME-$TAG-gateway

pushd src
zip -r ../out/$LAMBDA_NAME.zip .
popd

# Deploy function to LocalStack
echo "Deploying function..."
aws lambda create-function --function-name $LAMBDA_NAME --runtime python3.6 --handler handler.handler --zip-file fileb://out/$LAMBDA_NAME.zip --role arn:aws:iam::123456:role/irrelevant --region $REGION --endpoint http://localhost:4574
LAMBDA_ARN=$(aws lambda list-functions --query "Functions[?FunctionName==\`$LAMBDA_NAME\`].FunctionArn" --output text --region $REGION --endpoint http://localhost:4574)

# Create API Gateway
echo "Creating API Gateway..."
aws apigateway create-rest-api --name $GATEWAY_NAME --region $REGION --endpoint http://localhost:4567
API_ID=$(aws apigateway get-rest-apis --query "items[?name==\`$GATEWAY_NAME\`].id" --output text --region $REGION --endpoint http://localhost:4567)
PARENT_RESOURCE_ID=$(aws apigateway get-resources --rest-api-id $API_ID --query 'items[?path==`/`].id' --output text --region $REGION --endpoint http://localhost:4567)

# Create API Gateway resource
echo "Creating method and integration..."
RESOURCE_ID=$(aws apigateway get-resources --rest-api-id $API_ID --query 'items[?path==`/`].id' --output text --region $REGION --endpoint http://localhost:4567)

aws apigateway put-method \
    --rest-api-id $API_ID \
    --resource-id $RESOURCE_ID \
    --http-method GET \
    --authorization-type "NONE" \
    --endpoint http://localhost:4567 \
    --region $REGION

aws apigateway put-integration \
    --region $REGION \
    --rest-api-id $API_ID \
    --resource-id $RESOURCE_ID \
    --http-method GET \
    --type AWS_PROXY \
    --integration-http-method POST \
    --uri arn:aws:apigateway:$REGION:lambda:path/2015-03-31/functions/$LAMBDA_ARN/invocations \
    --passthrough-behavior WHEN_NO_MATCH \
    --endpoint http://localhost:4567 \
    --region $REGION

aws apigateway create-deployment \
    --rest-api-id $API_ID \
    --stage-name $STAGE \
    --endpoint http://localhost:4567 \
    --region $REGION

echo "Done!"
echo "API available at: http://localhost:4567/restapis/$API_ID/$STAGE/_user_request_/"

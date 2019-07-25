
PUSHD ..\..

IF NOT EXIST localstack (
  echo installing localstack...
  git clone https://github.com/localstack/localstack.git
  echo localstack installed
)
echo starting localstack...
PUSHD localstack
SET SERVICES="sns,sqs,kinesis,dynamodb,cloudwatch,lambda,apigateway"
SET DEFAULT_REGION=eu-west-1
SET LAMBDA_EXECUTOR=docker-reuse
docker-compose up -d
echo localstack started
POPD

POPD


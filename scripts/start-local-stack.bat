
PUSHD ..\..

IF NOT EXIST localstack (
  echo installing localstack...
  git clone https://github.com/localstack/localstack.git
  echo localstack installed
)
echo starting localstack...
PUSHD localstack
SET SERVICES="sns,sqs,kinesis,dynamodb,cloudwatch,s3,firehose"
SET DEFAULT_REGION=eu-west-1 
docker-compose up -d
echo localstack started
POPD

POPD


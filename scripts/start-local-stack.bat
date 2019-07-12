
PUSHD ..\..

IF NOT EXIST localstack (
  echo installing localstack...
  git clone https://github.com/localstack/localstack.git
  echo localstack installed
)
echo starting localstack...
PUSHD localstack
SERVICES="sns,sqs,kinesis,dynamodb,cloudwatch" DEFAULT_REGION=eu-west-1 docker-compose up -d
echo localstack started
POPD

POPD


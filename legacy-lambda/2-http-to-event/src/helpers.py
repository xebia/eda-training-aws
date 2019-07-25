import boto3
import os


def get_sqs():
    return boto3.resource(
        "sqs",
        endpoint_url="http://%s:4576" % (os.environ["LOCALSTACK_HOSTNAME"]),
        region_name="eu-west-1",
    )

import json

from helpers import get_sqs


def success(body):
    return {"statusCode": 200, "body": json.dumps(body)}


def handler(event, context):
    """
    Exercise 5a
    Task: take the payload that was POSTed over HTTP and put it on the `orderShipped` queue.
    For this exercise to succeed do the following:
    - Get a handle to SQS by calling `get_sqs()`
    - Retrieve a handle to the queue by calling `sqs.get_queue_by_name(QueueName='orderShipped')`
    - Get the POSTed payload from the Lambda event using `event["body"]`
    - Use `queue.send_message(MessageBody=body)` to send message to queue
    - Return a success response by calling `success(body)` where body can be anything
    """
    # Get handle to SQS and queue

    # Put message on queue

    # Notify success

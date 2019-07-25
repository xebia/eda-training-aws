import json
import requests


def failure(msg):
    raise Exception(msg)


def success(body):
    return {"statusCode": 200, "body": json.dumps(body)}


def handler(event, context):
    """
    Exercise 4
    Task: use the consumed SQS message in `event` to perform a POST call on the legacy Inventory service
    We've added some code to help you get the message (there might be multiple) from the Lambda event.
    For this exercise to succeed do the following:
    - Grab the records from the Lambda event (under the `Records` key in the `event` dict)
    - For each message:
        - Grab the message body (using message["body"]) and parse it into a python dict using `json.loads(body)`
        - Use the `requests` library to perform a HTTP POST to http://localhost:9010/inventory-api/v1/shipments
    - When all messages are processed, return a success message (using the success function, body can be anything)
    """
    # Grab records
    records = []

    # For each SQS message
    for message in records:
        # Grab body

        # Perform POST request
        # r = requests.post(url, json=body)
        pass

    # Notify success

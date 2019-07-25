# import json


def handler(event, context):
    """
    Exercise 4
    Task: use the consumed SQS message in `event` to perform a POST call on the legacy Inventory service
    For this exercise to succeed do the following:
    - Grab the messages from the `event` parameter using event["Records"]. Multiple messages might be available.
    - For each message:
        - Grab the message body (using message["body"]) and parse the JSON
        - Use the `requests` library to perform a HTTP POST to http://localhost:9010/inventory-api/v1/shipments
        - Rely on the retry mechanism of SQS to make sure the POST is repeated until you get a 200 OK response code
    """
    records = event["Records"]
    print(records)


import json
import os
import boto3
from datetime import datetime, timezone
from botocore.exceptions import ClientError

dynamodb = boto3.resource("dynamodb")
ses = boto3.client("ses")

def lambda_handler(event, context):
    for record in event["Records"]:
        message = None
        try:
            message = json.loads(record["body"])
            print(json.dumps({
                "event": "MESSAGE_RECEIVED",
                "messageId": message.get("id")
            }))

            send_email(message)
            save_message(message)

            print(json.dumps({
                "event": "MESSAGE_PROCESSED",
                "messageId": message.get("id")
            }))
        except Exception as e:
            print(json.dumps({
                "event": "MESSAGE_PROCESSING_FAILED",
                "messageId": message.get("id") if message else None,
                "error": str(e)
            }))
            raise e
    return {
        "statusCode": 200,
        "body": "Messages processed successfully"
    }

def save_message(message):
    table = dynamodb.Table(os.environ["TABLE_NAME"])
    item = {
        "type": message["type"],
        "id": message["id"],
        "sender_name": message["senderName"],
        "sender_email": message["senderEmail"],
        "sender_contact": message["senderContact"],
        "subject": message["subject"],
        "message_body": message["messageBody"],
        "timestamp": message["timestamp"],
        "created_at": datetime.now(timezone.utc).isoformat()
    }

    try:
        table.put_item(
            Item=item,
            ConditionExpression="attribute_not_exists(id)"
        )
    except ClientError as e:
        if e.response["Error"]["Code"] == "ConditionalCheckFailedException":
            print(json.dumps({
                "event": "DUPLICATE_MESSAGE_SKIPPED",
                "messageId": message["id"]
            }))
            return
        raise e

def send_email(message):
    email_body = f"""
New portfolio contact message received.

Name:
{message['senderName']}

Email:
{message['senderEmail']}

Contact:
{message['senderContact']}

Message:

{message['messageBody']}
"""

    try:
        ses.send_email(
            Source=os.environ["FROM_EMAIL"],
            Destination={
                "ToAddresses": [
                    os.environ["TO_EMAIL"]
                ]
            },
            ReplyToAddresses=[
                message["senderEmail"]
            ],
            Message={
                "Subject": {
                    "Data": message["subject"]
                },
                "Body": {
                    "Text": {
                        "Data": email_body
                    }
                }
            }
        )
    except ClientError as e:
        print(json.dumps({
            "event": "SES_EMAIL_FAILED",
            "messageId": message.get("id"),
            "error": e.response['Error']['Message']
        }))
        raise e

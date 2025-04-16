import os
from flask import Flask
from kafka import KafkaProducer, KafkaConsumer
import json
from datetime import datetime
import threading

app = Flask(__name__)
#fetch the HOST_MACHINE environment variable
HOST_MACHINE = os.environ.get('HOST_MACHINE')
# Kafka Configuration
KAFKA_BOOTSTRAP_SERVERS = [HOST_MACHINE+':9092']
KAFKA_TOPIC = 'test-topic'


# Initialize Kafka producer with JSON serialization
producer = KafkaProducer(
    bootstrap_servers=KAFKA_BOOTSTRAP_SERVERS
#    value_serializer=lambda v: json.dumps(v).encode('utf-8'),
#    retries=5,
#    acks='all'
)

# Initialize Kafka consumer
consumer = KafkaConsumer(
    KAFKA_TOPIC,
    bootstrap_servers=KAFKA_BOOTSTRAP_SERVERS
#    value_deserializer=lambda x: json.loads(x.decode('utf-8')),
#    auto_offset_reset='latest',
#    enable_auto_commit=True,
#    group_id='all-service-group'
)

"""
def kafka_consumer_thread():
  #
    for message in consumer:
        try:
            # Process the message
            print(f"Received message: {message.value}")
            # Add your message processing logic here
        except Exception as e:
            print(f"Error processing message: {e}")

# Start consumer thread
consumer_thread = threading.Thread(target=kafka_consumer_thread, daemon=True)
consumer_thread.start()
"""

@app.route('/helloworld')
def hello():
    return {"message": "Hello, World!"}

@app.route('/worldhello/')
def world():
    return {"message": "World, Hello!"}

@app.route('/push-message')
def push_message():
    print(HOST_MACHINE)
    try:
        # Get the current timestamp
        timestamp = datetime.now().strftime("%Y-%m-%d %H:%M:%S")

        # Create a message with the timestamp
        message = {"timestamp": timestamp}

        # Send the message to Kafka
        producer.send(KAFKA_TOPIC, message)
        producer.flush()

        return {"message": "Message sent successfully", "timestamp": timestamp}
    except Exception as e:
        return {"error": str(e)}, 500

@app.route('/receive-message')
def receive_message():
    try:
        # Receive a message from Kafka
        message = next(consumer)
        return {"message": message.value}
    except Exception as e:
        return {"error": str(e)}, 500

if __name__ == '__main__':
    app.run(host='0.0.0.0')

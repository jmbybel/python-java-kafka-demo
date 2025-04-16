a simple demo project of a Java and Python instance sharing a kafka queue.

Currently WIP: The Javan and Python containers can't connect to the Kafka container, likely due to incorrect boilerplate from Amazon Q

# Overview

The two services are in their own separate subproject, and each contains a dockerfile. 

The Java app is a simple Spring application with only 2 endpoints

The Python side is using Flask for similar effect.

They contain identical APIs to allow me to work on the specifics of what I already know (Spring) and what I wanted to learn (Flask, Kafka)

The APIS:
- /hello - simple hello world
- /worldhello - Direct api call to the other service and forward the result to the user  - hello world from the other box.
- /push-message  - Push a message into the Kafka queue
- /receive-message - Read a message in the Kafka queue.

We also have a full-project docker-compose to create the Kafka and Zookeeper instances needed as well as order dependencies.


# extra configuration

since i'm running this as a monorepo some extra things happened

To expose the python container to the java container, need to include the docker internal gateway in the run cmd, not unexpected

But when operating individual Docker containers through VSCode need to add that in the extension's settings file.

--add-host=host.docker.internal:host-gateway

The above doesn't matter - and doesn't work - with the docker-compose at the higher level.




# Issues/TODOs

On Docker compose-up, The init-kafka container doesn't properly reach into the Kafka container and create the "test-topic" topic that I would like

The webservice containers can't directly reach the Kafka queue to actually push/receive messages yet, likely port exposure or network-mode incorrect in the docker-compose.

Very dumb issue when doing the Docker compose-up more than once: The cluster ID created from a previous is not deleted. Need to manually delete the Kafka container's meta.properties




a simple demo project of a Java and Python instance sharing a kafka queue.

Each is contained in a subproject, and each contains a dockerfile.

The Java app is a simple Spring application with only 2 endpoints

The Python side is using Flask for similar effect.

However we also have a full-project docker-compose to create the Kafka and Zookeeper instances needed as well as order dependencies.




# extra configuration

since i'm running this as a monorepo some extra things happened

To expose the python container to the java container, need to include the docker internal gateway in the run cmd, not unexpected

But when operating individual Docker containers through VSCode need to add that in the extension's settings file.

--add-host=host.docker.internal:host-gateway

The above doesn't matter - and doesn't work - with the docker-compose at the higher level.




# Issues

Very dumb issue when doing the Docker compose-up more than once: The cluster ID created from a previous is not deleted

Zookeeper starts fine, but the Kafka instance fails, cascading the rest.

Manually deleting the 
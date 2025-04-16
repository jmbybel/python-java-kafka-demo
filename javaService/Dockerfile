# Use Java 17 JRE as base image
FROM eclipse-temurin:17-jre-jammy

# Set working directory
WORKDIR /javaService

# Create a non-root user for security
RUN addgroup --system javauser && adduser --system --group javauser

# NOTE: due to some oddity with VSCdoe x docker, it doesn't want to find the build/libs directory, so i manually copy the app.jar to the base directory of this project on a build.
# Copy the existing jar file
COPY build/libs/kafkademo-0.0.1-SNAPSHOT.jar app.jar

# Set ownership to non-root user
RUN chown -R javauser:javauser /javaService

# Switch to non-root user
USER javauser

# Expose port 8080
EXPOSE 8080

# Set Java options
ENV JAVA_OPTS="-Xms512m -Xmx512m" \
    HOST_MACHINE="host.docker.internal"


# Run the jar file
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -Dspring.profiles.active=docker -Dserver.address=0.0.0.0 -jar app.jar"]
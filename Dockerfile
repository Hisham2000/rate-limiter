# Step 1: Use an official JDK as the base image
FROM openjdk:17-jdk-alpine

# Step 2: Set the working directory inside the container
WORKDIR /app

# Step 3: Copy the JAR file of your Spring Boot application to the container
COPY target/rate-limiter-0.0.1-SNAPSHOT.jar /app/rate-limiter.jar

# Step 4: Specify the entry point to run the Spring Boot application
ENTRYPOINT ["java", "-jar", "/app/rate-limiter.jar"]

# Optional: Expose the port Spring Boot will run on (default is 8080)
EXPOSE 8080

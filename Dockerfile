# Stage 1: The "Builder" Stage
# We use an official Maven image with Java 17 to build our application.
FROM maven:3.9-openjdk-17 AS builder

# Set the working directory for the build
WORKDIR /build

# Copy the pom.xml file first. This is a trick to make builds faster.
COPY pom.xml .

# Download all the dependencies
RUN mvn dependency:go-offline -B

# Copy the rest of your source code
COPY src ./src

# Build the application and package it into a .jar file
RUN mvn package -DskipTests


# Stage 2: The "Final" Stage
# We use a small, efficient Java image to run the application.
FROM eclipse-temurin:17-jre-alpine

# Set the working directory for the final image
WORKDIR /app

# Copy the built .jar file from the "builder" stage
# IMPORTANT: Make sure the name "SmartToolsHub-0.0.1-SNAPSHOT.jar" is EXACTLY correct!
COPY target/SmartToolsHub-0.0.1-SNAPSHOT.jar /app/SmartToolsHub-0.0.1-SNAPSHOT.jar

# Tell the server that your application listens on port 8080
EXPOSE 8080

# The command to start your application
CMD ["java", "-jar", "SmartToolsHub-0.0.1-SNAPSHOT.jar"]

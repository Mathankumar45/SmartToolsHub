# Step 1: Tell Docker to use an official Java 17 image.
# This is like installing Java on the server.
FROM eclipse-temurin:17-jre-alpine

# Step 2: Set a working directory inside the server.
# This is like creating a folder called 'app' on the server.
WORKDIR /app

# Step 3: Copy your application's jar file into the server.
# IMPORTANT: You MUST change "my-app.jar" to your actual jar file name.
# You can find this name inside the 'target' folder of your project.
COPY target/SmartToolsHub-0.0.1-SNAPSHOT.jar /app/SmartToolsHub-0.0.1-SNAPSHOT.jar

# Step 4: Tell the server that your application listens on port 8080.
# This allows traffic from the internet to reach your app.
EXPOSE 8080

# Step 5: The command to start your application.
# IMPORTANT: You MUST change "my-app.jar" to the same name as above.
CMD ["java", "-jar", "SmartToolsHub-0.0.1-SNAPSHOT.jar.jar"]
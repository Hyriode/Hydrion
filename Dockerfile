# Build Application Jar
FROM gradle:7.4.2-jdk18 AS build

WORKDIR /usr/app/

# Copy Hylios project files
COPY . .

# Get username and token used in build.gradle
ARG USERNAME
ARG TOKEN
ENV USERNAME=$USERNAME TOKEN=$TOKEN

RUN gradle shadowJar

# Run Application
FROM openjdk:18.0.1.1-jdk

# Set working directory
WORKDIR /usr/app/

# Get all environments variables
ENV MEMORY="1G"

# Copy previous builded Jar
COPY --from=build /usr/app/build/libs/Hydrion-all.jar /usr/app/Hydrion.jar

# Start application
ENTRYPOINT java -Xmx${MEMORY} -jar Hydrion.jar
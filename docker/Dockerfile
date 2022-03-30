# Build Application Jar
FROM gradle:7.4.1-jdk8 AS build

WORKDIR /usr/app/

# Copy Hyggdrasil project files
COPY . .

RUN gradle shadowJar

# Run Application
FROM openjdk:16-slim

WORKDIR /usr/app/

# Copy previous builded Jar
COPY --from=build /usr/app/build/libs/Hydrion-all.jar /usr/app/Hydrion.jar

ENTRYPOINT java -Xmx${MEMORY} -jar Hydrion.jar
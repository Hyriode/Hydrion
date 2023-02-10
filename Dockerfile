# Build Application Jar
FROM gradle:7.6.0 AS build

WORKDIR /usr/app/

# Copy Hydrion project files
COPY . .

# Get username and token used in build.gradle
ARG USERNAME
ARG TOKEN
ENV USERNAME=$USERNAME TOKEN=$TOKEN

RUN gradle shadowJar

# Run Application
FROM openjdk:18.0.1.1-jdk

VOLUME ["/hydrion"]
WORKDIR /hydrion

# Copy previous builded Jar
COPY --from=build /usr/app/build/libs/Hydrion-all.jar /usr/app/Hydrion.jar
# Copy entrypoint script
COPY --from=build /usr/app/docker-entrypoint.sh /usr/app/docker-entrypoint.sh

# Add permission to file
RUN chmod +x /usr/app/docker-entrypoint.sh

STOPSIGNAL SIGTERM

# Start application
ENTRYPOINT ["sh", "/usr/app/docker-entrypoint.sh"]
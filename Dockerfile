FROM openjdk:17-jdk
WORKDIR /app
RUN apt-get update && \
    apt-get install -y maven
COPY . /app
RUN mvn clean install
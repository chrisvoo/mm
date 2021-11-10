# syntax=docker/dockerfile:1

#
# Build stage
#
FROM maven:3.6.0-jdk-11-slim AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package

#
# Package
#
FROM amazoncorretto:17-alpine-jdk
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring
COPY --from=build /home/app/target/*.jar /usr/local/lib/filemanager.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/usr/local/lib/filemanager.jar"]
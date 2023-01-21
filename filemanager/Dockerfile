# syntax=docker/dockerfile:1

FROM maven:3.8.4 AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
# produce errors and run in non-interactive mode
RUN mvn clean -e -B package


FROM amazoncorretto:19-alpine
RUN apk update && apk add inotify-tools
WORKDIR /app
COPY --from=builder /app/target/filemanager.jar .
#CMD ["java", "-Xms100m", "-Xmx1g", "-jar", "./filemanager.jar"]
CMD ["java", "-jar", "./filemanager.jar"]
FROM ubuntu:latest as build
RUN apt-get update
RUN apt-get install openjdk-21-jdk -y
COPY . .
RUN ./gradlew bootJar --no-daemon

FROM amazoncorretto:21
EXPOSE 8080
COPY --from=build /build/libs/4yourlife-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java","-jar","app.jar"]
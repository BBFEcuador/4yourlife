FROM gradle:8-jdk21-corretto AS build
WORKDIR /home/gradle
COPY . .
RUN gradle flywayRepair
RUN gradle bootJar --no-daemon

FROM openjdk:21-jdk-slim
EXPOSE 8080
COPY --from=build /home/gradle/build/libs/4yourlife-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java","-jar","app.jar"]
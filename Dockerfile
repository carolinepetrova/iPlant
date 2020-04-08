FROM openjdk:8

EXPOSE 8080

ARG JAR_FILE=target/iplant.jar
COPY ${JAR_FILE} iplant.jar

ENTRYPOINT ["java", "-jar", "/iplant.jar"]
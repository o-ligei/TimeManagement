FROM openjdk:8-jdk-alpine
VOLUME /tmp
ADD timemanagement-0.0.1-SNAPSHOT.jar timemanagement-0.0.1-SNAPSHOT.jar
CMD ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/timemanagement-0.0.1-SNAPSHOT.jar"]

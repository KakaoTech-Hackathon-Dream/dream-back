FROM openjdk:17-jdk

COPY 0.0.1-SNAPSHOT.jar .

ENTRYPOINT ["java", "-Dspring.profiles.active=prod", "-jar", "./0.0.1-SNAPSHOT.jar"]
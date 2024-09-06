FROM openjdk:17-jdk

COPY dream_back-0.0.1-SNAPSHOT.jar .

ENTRYPOINT ["java", "-Dspring.profiles.active=prod", "-jar", "./dream_back-0.0.1-SNAPSHOT.jar"]
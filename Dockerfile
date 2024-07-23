FROM openjdk:17-jdk
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar
ENV JAVA_OPTS="-Djava.net.preferIPv4Stack=true -Djava.net.preferIPv4Addresses=true"
ENTRYPOINT ["java", "-Dspring.profiles.active=docker", "-Duser.timezone=Asia/Seoul", "-jar", "app.jar"]
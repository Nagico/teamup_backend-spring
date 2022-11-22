FROM amazoncorretto:17-alpine-jdk as springboot

RUN apk -U upgrade

RUN apk --no-cache upgrade musl curl

WORKDIR /etc/java

ARG JAR_FILE=target/*.jar

COPY ${JAR_FILE} app.jar

EXPOSE 8080

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/etc/java/app.jar"]
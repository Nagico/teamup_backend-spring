FROM amazoncorretto:17-alpine-jdk as spring

RUN apk -U upgrade

RUN apk add curl

RUN apk upgrade musl

WORKDIR /etc/spring

COPY target/lib lib

COPY target/app.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/etc/spring/app.jar"]
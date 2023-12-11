FROM gradle:7.5.1-jdk17  AS build
WORKDIR /home/gradle/src
COPY --chown=gradle:gradle . /home/gradle/src
RUN gradle build -x test --no-daemon

FROM openjdk:17.0.1-jdk-slim

EXPOSE 8080 7001

RUN mkdir /app

COPY --from=build /home/gradle/src/build/libs/stock_exchanger.jar /app/stock_exchanger.jar

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom","-jar","/app/stock_exchanger.jar"]
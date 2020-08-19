FROM java:8-jre

MAINTAINER Vladyslav Yemelianov <emelyanov.vladyslav@gmail.com>

ADD ./target/na-client-bot.jar /app/
CMD ["java", "-Xmx400m", "-jar", "/app/na-client-bot.jar"]

EXPOSE 8080
FROM adoptopenjdk/openjdk11

MAINTAINER Vladyslav Yemelianov <emelyanov.vladyslav@gmail.com>

ADD target/na-support-bot.jar /app/
CMD ["java", "-Xmx200m", "-jar", "/app/na-support-bot.jar"]

EXPOSE 8076
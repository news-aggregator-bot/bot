FROM adoptopenjdk/openjdk11

MAINTAINER Vladyslav Yemelianov <emelyanov.vladyslav@gmail.com>

ADD target/na-admin-bot.jar /app/
CMD ["java", "-Xmx200m", "-jar", "/app/na-admin-bot.jar"]

EXPOSE 8075
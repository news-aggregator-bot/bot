FROM adoptopenjdk/openjdk15

MAINTAINER Vladyslav Yemelianov <emelyanov.vladyslav@gmail.com>

ADD ./target/na-client-bot.jar /app/
CMD ["java", "-Xmx512m", "-jar", "/app/na-client-bot.jar"]

EXPOSE 8080
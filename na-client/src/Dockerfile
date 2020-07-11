FROM java:8-jre

MAINTAINER Vladyslav Yemelianov <emelyanov.vladyslav@gmail.com>

ADD ./target/na-client.jar /app/
CMD ["java", "-Xmx400m", "-jar", "/app/na-client.jar"]

EXPOSE 8070
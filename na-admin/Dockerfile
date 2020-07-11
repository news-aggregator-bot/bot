FROM java:8-jre

MAINTAINER Vladyslav Yemelianov <emelyanov.vladyslav@gmail.com>

ADD ./target/na-admin.jar /app/
CMD ["java", "-Xmx200m", "-jar", "/app/na-admin.jar"]

EXPOSE 8075
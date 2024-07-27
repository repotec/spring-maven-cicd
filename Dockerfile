FROM openjdk:17-alpine
EXPOSE 9090
ADD target/*.jar employee-api.jar
ENV JAVA_OPTS=""
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /employee-api.jar" ]

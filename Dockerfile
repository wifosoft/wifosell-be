FROM openjdk:18-ea-11-jdk-alpine3.15
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
COPY cert /home/cert
EXPOSE 8888
ENTRYPOINT ["java","-jar","/app.jar"]
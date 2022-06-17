FROM openjdk:18-ea-11-jdk-alpine3.15
#WORKDIR /home/app
RUN addgroup -S spring && adduser -S spring -G spring
RUN mkdir storage
RUN chown -R spring:spring storage
RUN chmod 777 storage

USER spring:spring
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENV UPLOAD_DIR=storage
EXPOSE 8888
ENTRYPOINT ["java","-jar","/app.jar", "-Dspring-boot.run.profiles=devdb"]
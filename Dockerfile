FROM openjdk:18-ea-11-jdk-alpine3.15
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
RUN mkdir /home/storage/upload
RUN chown -R spring:spring /home/storage/upload
RUN chmod 777 /home/storage/upload
ENV UPLOAD_DIR=/home/storage/upload
EXPOSE 8888
ENTRYPOINT ["java","-jar","/app.jar", "-Dspring-boot.run.profiles=devdb"]
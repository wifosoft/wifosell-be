FROM openjdk:17-ea-5-jdk-alpine
#WORKDIR /home/app
#RUN addgroup -S spring && adduser -S spring -G spring
RUN mkdir storage
#RUN chown -R spring:spring storage
RUN chmod 777 storage

RUN apk add --no-cache gcompat


RUN apk add java-snappy-native

#RUN APK add --no-cache gcompat

#USER spring:spring
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENV UPLOAD_DIR=storage
EXPOSE 8888
ENTRYPOINT ["java","-jar","/app.jar", "--spring.profiles.active=prodv2"]
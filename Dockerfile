FROM openjdk:17-jdk-alpine
COPY ./target/*.jar /app/rbac-service.jar
EXPOSE 8080
CMD [ "java", "-jar", "/app/rbac-service.jar" ]
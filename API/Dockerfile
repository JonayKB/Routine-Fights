FROM eclipse-temurin:17-jdk-alpine

RUN ls -la

COPY target/*.jar /app.jar

COPY src/main/resources/application.properties /application.properties

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app.jar", "--spring.config.location=classpath:/application.properties"]

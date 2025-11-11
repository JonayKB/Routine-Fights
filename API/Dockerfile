FROM eclipse-temurin:17-jdk-alpine

COPY target/*.jar /app.jar

RUN ls -la

COPY src/main/resources/application.properties /application.properties

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app.jar", "--spring.config.location=classpath:/application.properties"]

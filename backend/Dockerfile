# 1. Java 17-nin stabil versiyası
FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

# JAR faylını kopyalayarkən ulduz (*) qoyaq ki, versiya fərqli olsa da tapsın
COPY target/*.jar app.jar

EXPOSE 8020

ENTRYPOINT ["java", "-jar", "app.jar"]
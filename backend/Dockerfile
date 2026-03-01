# 1. Java 17-nin yüngül versiyasını götürürük
FROM eclipse-temurin:17-jdk-alpine

# 2. İşçi qovluq yaradırıq
WORKDIR /app

# 3. Bizim yaratdığımız JAR faylını konteynerin içine kopyalayırıq
# (target qovluğundakı faylın adının tam eyni olduğundan əmin ol)
COPY target/book-0.0.1-SNAPSHOT.jar app.jar

# 4. Proqramın 8020 portunda işlədiyini Docker-ə deyirik
EXPOSE 8020

# 5. Proqramı işə salırıq
ENTRYPOINT ["java", "-jar", "app.jar"]
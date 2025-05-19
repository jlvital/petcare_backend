# ╔══════════════════════════════════════════════════╗
# ║                  Build Stage                     ║
# ╚══════════════════════════════════════════════════╝
FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /app

# Aprovecha el caché de dependencias Maven (local .m2)
VOLUME ["/root/.m2"]

# Copiamos el descriptor de dependencias y resolvemos primero
COPY pom.xml .
RUN mvn dependency:go-offline

# Copia el código fuente y empaqueta (sin tests)
COPY src ./src
RUN mvn clean package -DskipTests

# ╔══════════════════════════════════════════════════╗
# ║                     Run Stage                    ║
# ╚══════════════════════════════════════════════════╝
FROM eclipse-temurin:21.0.2_13-jdk
WORKDIR /app

# Copia el artefacto generado en la etapa anterior
COPY --from=build /app/target/PetCare-0.0.1-SNAPSHOT.jar app.jar

ENV PORT=10000
EXPOSE ${PORT}

# ✅ Entrada segura (Render inyecta el PORT automáticamente)
ENTRYPOINT ["sh", "-c", "java -jar app.jar --server.port=${PORT}"]

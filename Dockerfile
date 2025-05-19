# ╔══════════════════════════════════════════════════╗
# ║                 Build Stage                      ║
# ╚══════════════════════════════════════════════════╝
FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /app

# Copia el archivo de configuración y el código fuente
COPY pom.xml . 
COPY src ./src

# Compila el proyecto (sin tests)
RUN mvn clean package -DskipTests

# ╔══════════════════════════════════════════════════╗
# ║                  Run Stage                       ║
# ╚══════════════════════════════════════════════════╝
FROM eclipse-temurin:21.0.2_13-jdk
WORKDIR /app

# Copia el JAR compilado desde la etapa anterior
COPY --from=build /app/target/PetCare-0.0.1-SNAPSHOT.jar app.jar

# Render establece el puerto automáticamente, pero lo definimos por si se usa localmente
ENV PORT=10000
EXPOSE ${PORT}

# Comando para iniciar la aplicación
ENTRYPOINT ["sh", "-c", "java -jar app.jar --server.port=${PORT}"]

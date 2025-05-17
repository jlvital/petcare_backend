# =============================
# Etapa de construcción (Build Stage)
# =============================
FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /app

# Copiar solo lo necesario primero para aprovechar cacheo
COPY pom.xml .
COPY src ./src

# Compilar la aplicación (sin tests)
RUN mvn clean package -DskipTests

# =============================
# Etapa de ejecución (Run Stage)
# =============================
FROM eclipse-temurin:21.0.2_13-jdk
WORKDIR /app

# Copiar el .jar generado explícitamente
COPY --from=build /app/target/PetCare-0.0.1-SNAPSHOT.jar app.jar

# Puerto que Render define por variable de entorno PORT
ENV PORT=10000

# Expone el puerto para ejecutar en local (Render lo ignora)
EXPOSE ${PORT}

# Comando de ejecución adaptado al puerto dinámico de Render
ENTRYPOINT ["sh", "-c", "java -jar app.jar --server.port=${PORT}"]

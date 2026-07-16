# ============================================
# ETAPA 1: BUILD
# ============================================
# Imagen con JDK para compilar. Se usa el Gradle Wrapper del proyecto
# (gradlew) en lugar de una imagen con Gradle preinstalado, para
# garantizar la misma versión de Gradle que se usa en desarrollo.
FROM eclipse-temurin:25-jdk-alpine AS builder

WORKDIR /build

# Copiar primero los archivos de configuración de Gradle para
# aprovechar el cache de capas de Docker: si no cambian, esta capa
# (con las dependencias ya descargadas) se reutiliza en builds futuros.
COPY gradlew settings.gradle.kts build.gradle.kts ./
COPY gradle ./gradle
RUN chmod +x gradlew
RUN ./gradlew dependencies --no-daemon

# Copiar el código fuente y compilar
COPY src ./src
RUN ./gradlew bootJar -x test --no-daemon

# ============================================
# ETAPA 2: RUNTIME
# ============================================
# Imagen ligera solo con JRE (sin JDK, sin Gradle, sin código fuente)
FROM eclipse-temurin:25-jre-alpine

# Usuario no-root para ejecutar la aplicación
RUN addgroup -S spring && adduser -S spring -G spring

WORKDIR /app

COPY --from=builder /build/build/libs/*.jar app.jar
RUN chown spring:spring app.jar

USER spring:spring

EXPOSE 8080

# El health check usa el context-path /api definido en application.yml
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8080/api/actuator/health || exit 1

ENV SPRING_PROFILES_ACTIVE=prod

ENTRYPOINT ["java", \
    "-Djava.security.egd=file:/dev/./urandom", \
    "-Xms256m", \
    "-Xmx512m", \
    "-jar", \
    "app.jar"]

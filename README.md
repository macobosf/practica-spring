# Fundamentos Spring Boot — Laboratorio 01

**Asignatura:** Programación y Plataformas Web  
**Institución:** Universidad Politécnica Salesiana  
**Carrera:** Ingeniería de Sistemas  
**Grupo:** ec.edu.ups.icc   
---

## Descripción

Este proyecto constituye el primer laboratorio de la asignatura de Programación y Plataformas Web. Su objetivo es verificar la correcta configuración del entorno de desarrollo y demostrar el funcionamiento básico de un servidor web embebido mediante la exposición de un endpoint REST de estado (`/api/status`).

---

## Tecnologías utilizadas

| Tecnología        | Versión     |
|-------------------|-------------|
| Java              | 25          |
| Spring Boot       | 4.1.0       |
| Gradle (Kotlin)   | --          |
| Spring Web MVC    | (incluido)  |
| JUnit Platform    | (incluido)  |

---

## Estructura del proyecto

```
fundamentos01/
├── src/
│   └── main/
│       └── java/
│           └── ec/edu/ups/icc/fundamentos01/
│               ├── Fundamentos01Application.java
│               └── controllers/
│                   └── StatusController.java
├── build.gradle.kts
└── settings.gradle.kts
```

---

## Endpoint disponible

| Método | Ruta          | Descripción                          |
|--------|---------------|--------------------------------------|
| GET    | `/api/status` | Retorna el estado actual del servidor |

**Ejemplo de respuesta:**

```json
{
  "status": "running",
  "service": "Spring Boot API",
  "timestamp": "2026-06-18T14:52:59.860601512"
}
```

---

## Ejecución del proyecto

```bash
./gradlew bootRun
```

El servidor inicia en el puerto `8080` de forma predeterminada.

---

## Evidencias

### 1. Verificación de la versión de Java

Salida del comando `java -version` en terminal, confirmando que el entorno de ejecución cumple con el requisito de Java 25.

> **Captura:** *(insertar captura de pantalla)*

---

### 2. Servidor Spring Boot en ejecución

Salida de la consola al iniciar la aplicación, donde se observa:

```
:: Spring Boot ::                (v4.1.0)
...
Tomcat started on port 8080
```

> **Captura:** *(insertar captura de pantalla)*

---

### 3. Endpoint `/api/status` funcionando

Respuesta JSON obtenida al acceder a `http://localhost:8080/api/status` desde el navegador o cliente HTTP (Postman / Bruno).

> **Captura:** *(insertar captura de pantalla)*

---

### 4. Listado de controladores en terminal

Salida del siguiente comando ejecutado desde la raíz del proyecto:

```bash
ls ./src/main/java/ec/edu/ups/icc/fundamentos01/controllers/
```

Resultado esperado:

```
StatusController.java
```

> **Captura:** *(insertar captura de pantalla)*

---

## Explicación personal

### Funcionamiento del endpoint `/api/status`

El endpoint `/api/status` representa el punto de entrada más básico de la API construida con Spring Boot. Al recibir una solicitud HTTP de tipo GET en esa ruta, el método `status()` del controlador `StatusController` es invocado automáticamente por el framework. Este método retorna un mapa de clave-valor que Spring convierte en formato JSON antes de enviar la respuesta al cliente. Los campos retornados incluyen el nombre del servicio, su estado actual y la marca de tiempo del momento exacto en que se procesó la solicitud, lo que permite verificar que el servidor se encuentra activo y respondiendo correctamente.

### Función general de Spring Boot en la creación del servidor

Spring Boot simplifica el proceso de configuración y puesta en marcha de aplicaciones Java al proporcionar un servidor web embebido (Apache Tomcat) que se inicia junto con la aplicación, eliminando la necesidad de desplegar el proyecto en un servidor externo. La anotación `@SpringBootApplication` activa la configuración automática del contexto de aplicación, lo que permite que Spring detecte y registre los controladores REST de forma automática. Gracias a este enfoque, el desarrollador puede concentrarse en la lógica del negocio en lugar de gestionar manualmente la infraestructura del servidor.

---

## Autor

| Campo       | Detalle                      |
|-------------|------------------------------|
| Nombre      | Marco Cobos                  |
| Correo      | marcocobos15@gmail.com       |
| Fecha       | Junio 2026                   |

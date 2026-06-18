# Fundamentos Spring Boot — Laboratorio 01

**Asignatura:** Programación y Plataformas Web  
**Institución:** Universidad Politécnica Salesiana  
**Carrera:** Ingeniería de Sistemas  
**Grupo:** ec.edu.ups.icc  
**Versión del proyecto:** 0.0.1-SNAPSHOT  

---

## Descripción

Este proyecto constituye el primer laboratorio de la asignatura de Programación y Plataformas Web. Su objetivo es verificar la correcta configuración del entorno de desarrollo y demostrar el funcionamiento básico de un servidor web embebido mediante la exposición de endpoints REST utilizando Spring Boot.

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
│       ├── java/
│       │   └── ec/edu/ups/icc/fundamentos01/
│       │       ├── Fundamentos01Application.java
│       │       ├── controllers/
│       │       │   └── StatusController.java
│       │       └── students/
│       │           ├── controllers/
│       │           │   └── StudentController.java
│       │           └── models/
│       │               └── Student.java
│       └── resources/
│           └── application.yml
├── build.gradle.kts
└── settings.gradle.kts
```

---

## Configuración

El archivo `application.yml` define el puerto y el prefijo base de la API:

```yaml
server:
  port: 8080
  servlet:
    context-path: /api

spring:
  application:
    name: fundamentos01
```

Todos los endpoints quedan bajo el prefijo `/api`.

---

## Endpoints disponibles

| Método | Ruta                  | Descripción                            |
|--------|-----------------------|----------------------------------------|
| GET    | `/api/status`         | Retorna el estado actual del servidor  |
| GET    | `/api/students`       | Retorna la lista de estudiantes        |
| GET    | `/api/students/count` | Retorna el total de estudiantes        |

**Ejemplo de respuesta — `/api/status`:**

```json
{
  "status": "running",
  "service": "Spring Boot API",
  "timestamp": "2026-06-18T14:52:59.860601512"
}
```

**Ejemplo de respuesta — `/api/students`:**

```json
[
  { "id": 1, "name": "Juan", "age": 30 },
  { "id": 2, "name": "Diego", "age": 10 }
]
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

![java -version](assets/Captura%20desde%202026-06-18%2015-22-13.png)

---

### 2. Servidor Spring Boot en ejecución

Salida de la consola al iniciar la aplicación, donde se observa el banner de Spring Boot y la confirmación de que Tomcat inició en el puerto 8080.

![Spring Boot corriendo](assets/Captura%20desde%202026-06-18%2016-08-11.png)

---

### 3. Endpoint `/api/status` funcionando

Respuesta JSON obtenida al acceder a `http://localhost:8080/api/status` desde el navegador.

![Endpoint /api/status](assets/Captura%20desde%202026-06-18%2014-55-07.png)

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

### 5. Endpoint `/api/students` funcionando

Respuesta JSON con la lista de estudiantes registrados en memoria.

![Endpoint /api/students](assets/Captura%20desde%202026-06-18%2016-11-17.png)

---

### 6. Endpoint `/api/students/count` funcionando

Respuesta con el total de estudiantes registrados.

![Endpoint /api/students/count](assets/Captura%20desde%202026-06-18%2016-11-24.png)

---

## Explicación personal

### Funcionamiento del endpoint `/api/status`

El endpoint `/api/status` representa el punto de entrada más básico de la API construida con Spring Boot. Al recibir una solicitud HTTP de tipo GET en esa ruta, el método `status()` del controlador `StatusController` es invocado automáticamente por el framework. Este método retorna un mapa de clave-valor que Spring convierte en formato JSON antes de enviar la respuesta al cliente. Los campos retornados incluyen el nombre del servicio, su estado actual y la marca de tiempo del momento exacto en que se procesó la solicitud, lo que permite verificar que el servidor se encuentra activo y respondiendo correctamente.

### Función general de Spring Boot en la creación del servidor

Spring Boot simplifica el proceso de configuración y puesta en marcha de aplicaciones Java al proporcionar un servidor web embebido (Apache Tomcat) que se inicia junto con la aplicación, eliminando la necesidad de desplegar el proyecto en un servidor externo. La anotación `@SpringBootApplication` activa la configuración automática del contexto de aplicación, lo que permite que Spring detecte y registre los controladores REST de forma automática. Gracias a este enfoque, el desarrollador puede concentrarse en la lógica del negocio en lugar de gestionar manualmente la infraestructura del servidor.

---

## Autor

| Campo       | Detalle                  |
|-------------|--------------------------|
| Nombre      | Marco Cobos              |
| Correo      | marcocobos15@gmail.com   |
| Fecha       | Junio 2026               |

# Fundamentos Spring Boot — Laboratorio 01

**Asignatura:** Programación y Plataformas Web  
**Institución:** Universidad Politécnica Salesiana  
**Carrera:** Ingeniería de Sistemas  
**Grupo:** ec.edu.ups.icc  
**Versión del proyecto:** 0.0.1-SNAPSHOT  

---

## Descripción general

Este repositorio agrupa las prácticas del primer laboratorio de la asignatura de Programación y Plataformas Web. Cada práctica amplía el proyecto con nuevas funcionalidades sobre el mismo servidor Spring Boot, el cual expone una API REST bajo el prefijo `/api`.

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

## Configuración del servidor

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

## Ejecución del proyecto

```bash
./gradlew bootRun
```

---

# Práctica 1 — Servidor y endpoints básicos

## Objetivo

Verificar la correcta configuración del entorno de desarrollo y demostrar el funcionamiento básico de un servidor web embebido mediante la exposición de endpoints REST de estado y listado de estudiantes.

---

## Estructura del proyecto — Práctica 1

```
fundamentos01/
├── src/main/java/ec/edu/ups/icc/fundamentos01/
│   ├── Fundamentos01Application.java
│   ├── core/
│   │   └── controllers/
│   │       └── StatusController.java
│   └── students/
│       ├── controllers/
│       │   └── StudentController.java
│       └── models/
│           └── Student.java
└── src/main/resources/
    └── application.yml
```

---

## Endpoints — Práctica 1

| Método | Ruta                  | Descripción                           |
|--------|-----------------------|---------------------------------------|
| GET    | `/api/status`         | Retorna el estado actual del servidor |
| GET    | `/api/students`       | Retorna la lista de estudiantes       |
| GET    | `/api/students/count` | Retorna el total de estudiantes       |

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

## Evidencias — Práctica 1

### 1. Verificación de la versión de Java

Salida del comando `java -version` en terminal, confirmando que el entorno cumple con el requisito de Java 25.

![java -version](assets/java-version.png)

---

### 2. Servidor Spring Boot en ejecución

Salida de la consola al iniciar la aplicación, donde se observa el banner de Spring Boot y la confirmación de que Tomcat inició en el puerto 8080.

![Spring Boot corriendo](assets/spring-boot-running.png)

---

### 3. Endpoint `/api/status` funcionando

Respuesta JSON obtenida al acceder a `http://localhost:8080/api/status` desde el navegador.

![Endpoint /api/status](assets/status-endpoint.png)

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

---

### 5. Endpoint `/api/students` funcionando

Respuesta JSON con la lista de estudiantes registrados en memoria.

![Endpoint /api/students](assets/students-get-all.png)

---

### 6. Endpoint `/api/students/count` funcionando

Respuesta con el total de estudiantes registrados.

![Endpoint /api/students/count](assets/students-count.png)

---

## Explicación personal — Práctica 1

### Funcionamiento del endpoint `/api/status`

El endpoint `/api/status` representa el punto de entrada más básico de la API construida con Spring Boot. Al recibir una solicitud HTTP de tipo GET en esa ruta, el método `status()` del controlador `StatusController` es invocado automáticamente por el framework. Este método retorna un mapa de clave-valor que Spring convierte en formato JSON antes de enviar la respuesta al cliente. Los campos retornados incluyen el nombre del servicio, su estado actual y la marca de tiempo del momento exacto en que se procesó la solicitud, lo que permite verificar que el servidor se encuentra activo y respondiendo correctamente.

### Función general de Spring Boot en la creación del servidor

Spring Boot simplifica el proceso de configuración y puesta en marcha de aplicaciones Java al proporcionar un servidor web embebido (Apache Tomcat) que se inicia junto con la aplicación, eliminando la necesidad de desplegar el proyecto en un servidor externo. La anotación `@SpringBootApplication` activa la configuración automática del contexto de aplicación, lo que permite que Spring detecte y registre los controladores REST de forma automática. Gracias a este enfoque, el desarrollador puede concentrarse en la lógica del negocio en lugar de gestionar manualmente la infraestructura del servidor.

---

# Práctica 2 — CRUD de productos y usuarios

## Objetivo

Implementar operaciones CRUD completas (Create, Read, Update, Delete) sobre dos recursos — productos y usuarios — aplicando una arquitectura en capas con controladores, servicios, repositorios, entidades y DTOs.

---

## Estructura del proyecto — Práctica 2

```
fundamentos01/
└── src/main/java/ec/edu/ups/icc/fundamentos01/
    ├── products/
    │   ├── controllers/
    │   │   └── ProductController.java
    │   ├── dtos/
    │   │   ├── CreateProductDto.java
    │   │   ├── UpdateProductDto.java
    │   │   ├── PartialUpdateProductDto.java
    │   │   └── ProductResponseDto.java
    │   ├── entities/
    │   │   └── ProductEntity.java
    │   ├── mappers/
    │   │   └── ProductMapper.java
    │   ├── models/
    │   │   └── ProductModel.java
    │   ├── repositories/
    │   │   └── ProductRepository.java
    │   └── services/
    │       ├── ProductService.java
    │       └── ProductServiceImpl.java
    └── users/
        ├── controllers/
        │   └── UserController.java
        ├── dtos/
        │   ├── CreateUserDto.java
        │   ├── UpdateUserDto.java
        │   ├── PartialUpdateUserDto.java
        │   └── UserResponseDto.java
        ├── entities/
        │   └── UserEntity.java
        ├── mappers/
        │   └── UserMapper.java
        ├── models/
        │   └── UserModel.java
        ├── repositories/
        │   └── UserRepository.java
        └── services/
            ├── UserService.java
            └── UserServiceImpl.java
```

---

## Endpoints — Práctica 2

### Productos

| Método | Ruta                  | Descripción                              |
|--------|-----------------------|------------------------------------------|
| GET    | `/api/products`       | Retorna la lista de todos los productos  |
| GET    | `/api/products/{id}`  | Retorna un producto por su id            |
| POST   | `/api/products`       | Crea un nuevo producto                   |
| PUT    | `/api/products/{id}`  | Actualiza completamente un producto      |
| PATCH  | `/api/products/{id}`  | Actualiza parcialmente un producto       |
| DELETE | `/api/products/{id}`  | Elimina un producto por su id            |

### Usuarios

| Método | Ruta               | Descripción                            |
|--------|--------------------|----------------------------------------|
| GET    | `/api/users`       | Retorna la lista de todos los usuarios |
| GET    | `/api/users/{id}`  | Retorna un usuario por su id           |
| POST   | `/api/users`       | Crea un nuevo usuario                  |
| PUT    | `/api/users/{id}`  | Actualiza completamente un usuario     |
| PATCH  | `/api/users/{id}`  | Actualiza parcialmente un usuario      |
| DELETE | `/api/users/{id}`  | Elimina un usuario por su id           |

---

## Evidencias — Práctica 2

### 7. POST `/api/products` — Crear producto

Creación de un nuevo producto enviando `name`, `price` y `stock` en el cuerpo de la petición. El servidor retorna el producto creado con su id asignado.

![POST /api/products](assets/products-post-create.png)

---

### 8. GET `/api/products` — Lista de productos

Respuesta JSON con los 3 productos registrados en memoria tras las peticiones POST previas (Laptop, Mouse, Teclado).

![GET /api/products](assets/products-get-all.png)

---

### 9. GET `/api/products/{id}` — Producto por id

Respuesta JSON al consultar el producto con id 2 (Mouse). El servidor retorna únicamente los datos del producto solicitado.

![GET /api/products/2](assets/products-get-one.png)

---

### 10. PUT `/api/products/{id}` — Actualización completa

Actualización total del producto con id 1. Se reemplazan todos los campos: el nombre cambia de `Laptop` a `Laptop Gaming`, el precio a `1850.0` y el stock a `5`.

![PUT /api/products/1](assets/products-put.png)

---

### 11. PATCH `/api/products/{id}` — Actualización parcial

Actualización parcial del producto con id 3. Solo se envía el campo `price` con el valor `39.99`. El nombre y el stock permanecen sin cambios.

![PATCH /api/products/3](assets/products-patch.png)

---

### 12. GET `/api/products` — Lista tras modificaciones

Lista actualizada de productos después de aplicar el PUT y el PATCH. Se confirma que los cambios persisten correctamente en memoria.

![GET /api/products tras modificaciones](assets/products-get-all-updated.png)

---

### 13. DELETE `/api/products/{id}` — Eliminar producto existente

Eliminación del producto con id 2 (Mouse). El servidor confirma la operación con el mensaje `Deleted successfully`.

![DELETE /api/products/2](assets/products-delete-ok.png)

---

### 14. DELETE `/api/products/{id}` — Eliminar producto inexistente

Intento de eliminación de un producto con un id que no existe. El servidor retorna el mensaje de error `Product not found`.

![DELETE /api/products inexistente](assets/products-delete-not-found.png)

---

## Explicación personal — Práctica 2

### Arquitectura en capas y uso de DTOs

La Práctica 2 introduce una arquitectura en capas donde cada componente tiene una responsabilidad específica. El controlador recibe las peticiones HTTP y las delega al servicio; el servicio contiene la lógica de negocio y accede al repositorio; el repositorio gestiona el almacenamiento en memoria. Los DTOs (Data Transfer Objects) permiten separar los datos que el cliente envía de los datos que el sistema almacena internamente, evitando exponer directamente las entidades del dominio.

### Manejo del timestamp con `LocalDateTime.now()`

Uno de los aspectos relevantes de la práctica es el control del campo `createdAt`. Aunque el DTO de creación (`CreateProductDto`) podría recibir una fecha enviada por el cliente, el backend la ignora intencionalmente. En su lugar, el mapper asigna la fecha en el momento exacto en que se procesa la petición:

```java
// ProductMapper.java
model.setCreatedAt(LocalDateTime.now());
```

Esto garantiza que la fecha de creación siempre sea generada por el servidor y no pueda ser manipulada externamente. Adicionalmente, el campo `createdAt` no se incluye en la respuesta (`ProductResponseDto`), lo que evita exponer información interna innecesaria al cliente.

### Generación del id con contador en memoria

Dado que el proyecto no utiliza base de datos, el id de cada producto se genera mediante un contador definido en el servicio. Cada vez que se crea un producto, el servicio asigna el valor actual del contador como id y luego lo incrementa en uno:

```java
// ProductServiceImpl.java
private Long currentId = 1L;

product.setId(currentId);
currentId++;
```

Este mecanismo garantiza que cada producto tenga un identificador único durante la ejecución del servidor, aunque los datos se pierden al reiniciarlo por tratarse de almacenamiento en memoria.

---

# Autor

| Campo       | Detalle                  |
|-------------|--------------------------|
| Nombre      | Marco Cobos              |
| Correo      | marcocobos15@gmail.com   |
| Fecha       | Junio 2026               |

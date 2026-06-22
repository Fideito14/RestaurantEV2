# menu-service — RestaurantEV2

## Contexto del proyecto

Microservicio encargado de la gestión del menú de un restaurante. Forma parte de una arquitectura distribuida compuesta por múltiples microservicios independientes (`menu-service`, `pedidos-service`, `gateway-service`, `eureka-server`, entre otros desarrollados por el equipo), cada uno con su propia base de datos y responsabilidad funcional, comunicados entre sí mediante REST, registrados dinámicamente en un servidor Eureka y centralizados a través de un API Gateway.

Este microservicio expone un CRUD completo de platos, permitiendo crear, consultar, actualizar y eliminar elementos del menú, además de búsquedas específicas por nombre y disponibilidad.

## Estudiante

- Juan Carvajal — `menu-service`, `pedidos-service`, `gateway-service` y `eureka-server`

## Microservicios implementados

| Microservicio | Puerto | Base de datos | Descripción |
|---|---|---|---|
| `eureka-server` | 8761 | — | Servidor de descubrimiento de servicios (service discovery) |
| `menu-service` | dinámico (registrado en Eureka) | `db_menu` (MySQL) | Gestión de platos del menú |
| `pedidos-service` | dinámico (registrado en Eureka) | `db_pedidos` (MySQL) | Gestión de pedidos, valida el plato contra `menu-service` vía LoadBalancer |
| `gateway-service` | 8080 | — | Punto de entrada único, resuelve servicios por nombre (`lb://`) |

## Tecnologías utilizadas

- Java 21
- Spring Boot 4.0.6
- Spring Data JPA / Hibernate
- MySQL 8.0
- Flyway (migraciones de base de datos)
- Lombok
- Spring HATEOAS
- SpringDoc OpenAPI (Swagger)
- Data Faker
- Spring Cloud Netflix Eureka Client (service discovery)
- Spring Cloud 2025.1.2 (BOM compatible con Spring Boot 4)
- JUnit 5 + Mockito
- Docker / Docker Compose

## Estructura del proyecto (patrón CSR)

```
com.ev2.menu_service
 ├── controller     → PlatoController (endpoints REST documentados con OpenAPI)
 ├── service        → PlatoService (lógica de negocio, logs)
 ├── repository     → PlatoRepository (JPQL personalizado)
 ├── model          → Plato (entidad JPA)
 ├── dto            → PlatoRequestDTO / PlatoResponseDTO
 ├── exception      → GlobalExceptionHandler
 └── config         → DataSeeder (Data Faker)
```

## Service Discovery con Eureka

`menu-service` arranca con **puerto dinámico** (`server.port: 0`) y se registra automáticamente en `eureka-server` al iniciar, informando su puerto real asignado por el sistema. Esto significa que el microservicio **no tiene una URL fija**: solo es accesible a través del `gateway-service`, que lo localiza dinámicamente por nombre lógico (`MENU-SERVICE`).

## Rutas principales del Gateway

El `gateway-service` centraliza el acceso a este microservicio en el puerto **8080**, resolviendo el destino real mediante Eureka:

```
http://localhost:8080/api/platos/**   →  lb://MENU-SERVICE  →  resuelto dinámicamente por Eureka
```

## Endpoints principales

| Método | Endpoint | Descripción |
|---|---|---|
| GET | `/api/platos` | Lista todos los platos |
| GET | `/api/platos/disponibles` | Lista solo los platos disponibles |
| GET | `/api/platos/buscar?nombre=` | Búsqueda parcial por nombre |
| GET | `/api/platos/{id}` | Obtiene un plato por ID |
| POST | `/api/platos` | Crea un nuevo plato |
| PUT | `/api/platos/{id}` | Actualiza un plato existente |
| DELETE | `/api/platos/{id}` | Elimina un plato |

> Todos los endpoints deben consumirse a través del Gateway (`http://localhost:8080/api/platos`), ya que el puerto del microservicio es dinámico.

## Documentación Swagger / OpenAPI

- **Local (ejecución directa desde IntelliJ):** http://localhost:8081/swagger-ui.html (cuando se ejecuta con puerto fijo para pruebas)
- **Con Eureka/Docker:** el puerto es dinámico; se recomienda consultar el panel de Eureka (`http://localhost:8761`) para obtener la URL real registrada y acceder a `/swagger-ui.html` desde ahí.

## Instrucciones de ejecución

### Ejecución local (IntelliJ / Maven)

1. Tener MySQL corriendo (Laragon u otro) en el puerto `3306`.
2. Crear la base de datos `db_menu` (Flyway crea las tablas automáticamente al arrancar).
3. Ejecutar primero `eureka-server` (puerto 8761).
4. Ejecutar la clase `MenuServiceApplication`. Tomará un puerto dinámico y se registrará en Eureka automáticamente.
5. Ejecutar `gateway-service` para acceder a través del puerto 8080.

### Ejecución con Docker Compose

Desde la carpeta raíz que contiene los 4 microservicios y el archivo `docker-compose.yml`:

```bash
docker-compose up --build
```

Esto levanta:
- `eureka-server` (puerto 8761 expuesto)
- `mysql-menu` (puerto 3307 expuesto, 3306 interno)
- `mysql-pedidos` (puerto 3308 expuesto, 3306 interno)
- `menu-service` (puerto dinámico, sin mapeo externo)
- `pedidos-service` (puerto dinámico, sin mapeo externo)
- `gateway-service` (puerto 8080 expuesto, único punto de acceso)

Todos los servicios se comunican entre sí a través de la red interna `restaurant-network`. Los healthchecks de MySQL garantizan que los microservicios esperen a que la base de datos esté lista antes de intentar conectarse, y `eureka-server` debe estar levantado antes de que los demás microservicios se registren correctamente.

### Pruebas

```bash
./mvnw test
```

Ejecuta las pruebas unitarias del `PlatoService` con JUnit 5 y Mockito.

## Pruebas unitarias

Cobertura de pruebas sobre `PlatoService`:
- Obtener todos los platos
- Obtener plato por ID (existente / no existente)
- Guardar un nuevo plato
- Actualizar un plato existente
- Eliminar un plato


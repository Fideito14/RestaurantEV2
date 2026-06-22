# pedidos-service — RestaurantEV2

## Contexto del proyecto

Microservicio encargado de la gestión de pedidos de un restaurante. Forma parte de una arquitectura distribuida compuesta por múltiples microservicios independientes (`menu-service`, `pedidos-service`, `gateway-service`, `eureka-server`, entre otros desarrollados por el equipo), cada uno con su propia base de datos y responsabilidad funcional, registrados dinámicamente en Eureka.

`pedidos-service` valida en tiempo real, mediante una llamada REST balanceada por Eureka hacia `menu-service`, que el plato indicado en un pedido exista antes de persistirlo, garantizando consistencia entre ambos dominios sin compartir base de datos ni depender de URLs fijas.

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
- RestTemplate con `@LoadBalanced` (comunicación REST balanceada vía Eureka)
- Spring Cloud Netflix Eureka Client + LoadBalancer
- Spring Cloud 2025.1.2 (BOM compatible con Spring Boot 4)
- JUnit 5 + Mockito
- Docker / Docker Compose

## Estructura del proyecto (patrón CSR)

```
com.ev2.pedidos_service
 ├── controller     → PedidoController (endpoints REST documentados con OpenAPI)
 ├── service        → PedidoService (lógica de negocio, validación remota, logs)
 ├── repository     → PedidoRepository (JPQL personalizado)
 ├── model          → Pedido (entidad JPA)
 ├── dto            → PedidoRequestDTO / PedidoResponseDTO
 ├── exception      → GlobalExceptionHandler
 └── config         → DataSeeder (Data Faker)
```

## Comunicación con menu-service vía Eureka

Antes de crear o actualizar un pedido, `PedidoService` consulta `GET http://MENU-SERVICE/api/platos/{id}` usando un `RestTemplate` anotado con `@LoadBalanced`. Esta anotación intercepta la llamada y resuelve el nombre lógico `MENU-SERVICE` consultando a Eureka, en lugar de depender de un host o puerto fijo (que ya no existe, pues `menu-service` corre en puerto dinámico).

Si el plato no existe, se lanza una excepción controlada que el `GlobalExceptionHandler` traduce en una respuesta **400 Bad Request** con un mensaje descriptivo.

## Rutas principales del Gateway

El `gateway-service` centraliza el acceso a este microservicio en el puerto **8080**:

```
http://localhost:8080/api/pedidos/**   →  lb://PEDIDOS-SERVICE  →  resuelto dinámicamente por Eureka
```

## Endpoints principales

| Método | Endpoint | Descripción |
|---|---|---|
| GET | `/api/pedidos` | Lista todos los pedidos |
| GET | `/api/pedidos/{id}` | Obtiene un pedido por ID |
| GET | `/api/pedidos/estado?estado=` | Filtra pedidos por estado |
| GET | `/api/pedidos/mesa?mesa=` | Filtra pedidos por mesa |
| GET | `/api/pedidos/plato/{platoId}` | Filtra pedidos por plato |
| POST | `/api/pedidos` | Crea un pedido (valida el plato en menu-service vía Eureka) |
| PUT | `/api/pedidos/{id}` | Actualiza un pedido (vuelve a validar el plato) |
| DELETE | `/api/pedidos/{id}` | Elimina un pedido |

> Todos los endpoints deben consumirse a través del Gateway (`http://localhost:8080/api/pedidos`), ya que el puerto del microservicio es dinámico.

## Documentación Swagger / OpenAPI

- **Local (ejecución directa desde IntelliJ):** http://localhost:8082/swagger-ui.html (cuando se ejecuta con puerto fijo para pruebas)
- **Con Eureka/Docker:** el puerto es dinámico; se recomienda consultar el panel de Eureka (`http://localhost:8761`) para obtener la URL real registrada y acceder a `/swagger-ui.html` desde ahí.

## Instrucciones de ejecución

### Ejecución local (IntelliJ / Maven)

1. Tener MySQL corriendo (Laragon u otro) en el puerto `3306`.
2. Crear la base de datos `db_pedidos` (Flyway crea las tablas automáticamente al arrancar).
3. Ejecutar primero `eureka-server` (puerto 8761).
4. Ejecutar `menu-service` (requisito para la validación de platos vía Eureka).
5. Ejecutar la clase `PedidosServiceApplication`. Tomará un puerto dinámico y se registrará en Eureka automáticamente.
6. Ejecutar `gateway-service` para acceder a través del puerto 8080.

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

Dentro de Docker, `pedidos-service` consulta a `menu-service` mediante `http://MENU-SERVICE/...` resuelto por Eureka (registrado en el contenedor `eureka-server`), no mediante hostname ni puerto fijo.

### Pruebas

```bash
./mvnw test
```

Ejecuta las pruebas unitarias del `PedidoService` con JUnit 5 y Mockito, incluyendo el mock de `RestTemplate` para simular la respuesta de `menu-service`.

## Pruebas unitarias

Cobertura de pruebas sobre `PedidoService`:
- Obtener todos los pedidos
- Obtener pedido por ID (existente / no existente)
- Filtrar por estado, mesa y plato
- Crear un pedido validando el plato vía RestTemplate (mockeado)
- Eliminar un pedido


# gateway-service — RestaurantEV2

## Contexto del proyecto

API Gateway que centraliza el acceso a los microservicios del proyecto RestaurantEV2 (`menu-service`, `pedidos-service`, entre otros desarrollados por el equipo), construido con Spring Cloud Gateway (WebFlux). Actúa como punto de entrada único, descubriendo dinámicamente la ubicación real de cada microservicio a través de `eureka-server` en lugar de depender de URLs fijas.

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
- Spring Boot 4.0.7
- Spring Cloud Gateway Server WebFlux
- Spring Cloud Netflix Eureka Client
- Spring Cloud LoadBalancer
- Spring Cloud 2025.1.2 (BOM compatible con Spring Boot 4)
- Docker / Docker Compose

## Rutas configuradas (Service Discovery)

| Path | Destino lógico (Eureka) | Microservicio |
|---|---|---|
| `/api/platos/**` | `lb://MENU-SERVICE` | menu-service |
| `/api/pedidos/**` | `lb://PEDIDOS-SERVICE` | pedidos-service |

A diferencia de un Gateway con rutas fijas (`http://localhost:8081`), este Gateway resuelve la ubicación real de cada microservicio en tiempo de ejecución consultando a `eureka-server`, ya que `menu-service` y `pedidos-service` corren en **puertos dinámicos**.

Todas las peticiones se reciben en el puerto **8080** y se redirigen automáticamente según el prefijo de la URL, balanceando la carga si hubiera múltiples instancias registradas del mismo servicio.

## Ejemplo de uso

```
GET http://localhost:8080/api/platos        → resuelto vía Eureka hacia MENU-SERVICE
GET http://localhost:8080/api/pedidos       → resuelto vía Eureka hacia PEDIDOS-SERVICE
```

## Instrucciones de ejecución

### Ejecución local (IntelliJ / Maven)

1. Ejecutar primero `eureka-server` (puerto 8761).
2. Ejecutar `menu-service` y `pedidos-service` (toman puerto dinámico y se registran solos).
3. Ejecutar la clase `GatewayServiceApplication`.
4. El gateway queda disponible en `http://localhost:8080`, resolviendo las rutas automáticamente.

### Ejecución con Docker Compose

Desde la carpeta raíz que contiene los 4 microservicios y el archivo `docker-compose.yml`:

```bash
docker-compose up --build
```

El gateway depende de que `eureka-server`, `menu-service` y `pedidos-service` estén levantados, y se comunica con `eureka-server` usando su nombre de contenedor (`http://eureka-server:8761/eureka/`) dentro de la red `restaurant-network`.

## Configuración YAML

La configuración de rutas y de Eureka Client se encuentra en `application.yml`, usando el formato declarativo de Spring Cloud Gateway con `predicates` basados en `Path` y URIs `lb://` resueltas dinámicamente.


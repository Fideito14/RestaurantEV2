# eureka-server — RestaurantEV2

## Contexto del proyecto

Servidor de descubrimiento de servicios (Service Discovery) basado en Netflix Eureka, parte de la arquitectura distribuida del proyecto RestaurantEV2. Actúa como un directorio central donde `menu-service`, `pedidos-service` y `gateway-service` se registran automáticamente al arrancar, permitiendo que se localicen entre sí dinámicamente sin depender de URLs o puertos fijos.

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
- Spring Cloud Netflix Eureka Server
- Spring Cloud 2025.1.2 (BOM compatible con Spring Boot 4)
- Docker / Docker Compose

## ¿Por qué Eureka?

Como `menu-service` y `pedidos-service` corren en **puertos dinámicos** (`server.port: 0`), no es posible que el Gateway u otros microservicios los ubiquen mediante una URL fija. Eureka resuelve este problema: cada microservicio se registra a sí mismo con su nombre lógico (`MENU-SERVICE`, `PEDIDOS-SERVICE`) y el puerto real asignado, y cualquier otro servicio puede consultarlo por nombre en lugar de por dirección.

## Panel de administración

Eureka expone una interfaz web donde se pueden ver todos los servicios registrados, su estado (`UP`/`DOWN`) y la URL real donde están corriendo:

```
http://localhost:8761
```

## Configuración

El servidor está configurado para **no registrarse a sí mismo** ni intentar descubrir otros registros (ya que él es el único registro):

```yaml
eureka:
  client:
    register-with-eureka: false
    fetch-registry: false
  server:
    enable-self-preservation: false
```

## Instrucciones de ejecución

### Ejecución local (IntelliJ / Maven)

Este debe ser el **primer** proyecto en arrancar, ya que los demás microservicios necesitan registrarse contra él al iniciar.

1. Ejecutar la clase `EurekaServerApplication`.
2. El servidor queda disponible en `http://localhost:8761`.
3. Luego arrancar `menu-service`, `pedidos-service` y finalmente `gateway-service`.

### Ejecución con Docker Compose

Desde la carpeta raíz que contiene los 4 microservicios y el archivo `docker-compose.yml`:

```bash
docker-compose up --build
```

`eureka-server` se levanta como el primer contenedor dependiente, y los demás microservicios apuntan a él usando su nombre de contenedor (`http://eureka-server:8761/eureka/`) dentro de la red `restaurant-network`.


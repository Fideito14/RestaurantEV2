spring.application.name=mesa
server.port=8080

spring.datasource.url=jdbc:mysql://localhost:3306/mesa_db
spring.datasource.username=root
spring.datasource.password=
spring.jpa.hibernate.ddl-auto=none
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.show-sql=true

# Flyway
spring.flyway.enabled=true
spring.flyway.locations=classpath:db/migration

#URL del microservicio usuario
ms.usuarios.url=http://localhost:8081
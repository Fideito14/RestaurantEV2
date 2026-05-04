CREATE TABLE mesas (
    id_mesa INT AUTO_INCREMENT PRIMARY KEY,
    numero_mesa INT NOT NULL UNIQUE,
    estado VARCHAR(20) NOT NULL,
    usuario_id INT NULL
);

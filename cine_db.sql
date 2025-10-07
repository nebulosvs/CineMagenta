-- Parte 1: Creación de la base de datos y la tabla
-- Crear la base de datos si no existe
DROP DATABASE Cine_DB;
CREATE DATABASE IF NOT EXISTS Cine_DB;
-- Usar la base de datos recién creada
USE Cine_DB;
-- Crear la tabla 'Cartelera'
CREATE TABLE IF NOT EXISTS Cartelera (
 id INT AUTO_INCREMENT PRIMARY KEY,
 titulo VARCHAR(150) NOT NULL,
 director VARCHAR(50),
 anio INT NOT NULL,
 duracion INT,
 genero VARCHAR(50),
 CHECK (genero IN ('Comedia', 'Drama', 'Accion', 'Ciencia ficcion', 'Terror', 'Animacion', 'Aventura', 'Musical'))
);
-- Parte 2: Inserción de datos de ejemplo
-- Insertar una película para cada género
INSERT INTO Cartelera (titulo, director, anio, duracion, genero) VALUES
('Supercool', 'Greg Mottola', 2007, 113, 'Comedia'),
('El Padrino', 'Francis Ford Coppola', 1972, 175, 'Drama'),
('Duro de matar', 'John McTiernan', 1988, 132, 'Accion'),
('Interestelar', 'Christopher Nolan', 2014, 169, 'Ciencia ficcion'),
('El Conjuro', 'James Wan', 2013, 112, 'Terror'),
('El rey león', 'Roger Allers', 1994, 88, 'Animacion'),
('Parque Jurásico', 'Steven Spielberg', 1993, 127, 'Aventura');

-- Parte 3: Creación de los SP
-- LISTAR
DELIMITER //
CREATE PROCEDURE sp_listar_peliculas()
BEGIN
SELECT id, titulo, director, anio, duracion, genero
FROM Cartelera
ORDER BY anio ASC, titulo ASC;
END //
DELIMITER;

-- FILTRAR
DELIMITER //
CREATE PROCEDURE sp_filtrar_peliculas(
    IN p_genero_like VARCHAR(50),
    IN p_anio_desde  INT,
    IN p_anio_hasta  INT)
BEGIN
SELECT id, titulo, director, anio, duracion, genero
FROM Cartelera
WHERE (p_genero_like IS NULL OR LOWER(genero) LIKE CONCAT('%', LOWER(p_genero_like), '%'))
AND (p_anio_desde  IS NULL OR anio >= p_anio_desde)
AND (p_anio_hasta  IS NULL OR anio <= p_anio_hasta)
ORDER BY anio ASC, titulo ASC;
END // 
DELIMITER;


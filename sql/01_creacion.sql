-- Ruta del Programador - Creación de la base de datos (MySQL 8.0)
DROP DATABASE IF EXISTS ruta_programador;
CREATE DATABASE ruta_programador CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE ruta_programador;

CREATE TABLE franja_etaria (
  id_franja     INT AUTO_INCREMENT PRIMARY KEY,
  nombre        VARCHAR(30) NOT NULL UNIQUE,
  edad_minima   TINYINT NOT NULL,
  edad_maxima   TINYINT NOT NULL,
  CHECK (edad_minima < edad_maxima)
);

CREATE TABLE mundo (
  id_mundo  INT AUTO_INCREMENT PRIMARY KEY,
  nombre    VARCHAR(50) NOT NULL UNIQUE,
  concepto  VARCHAR(255) NOT NULL,
  orden     TINYINT NOT NULL UNIQUE
);

CREATE TABLE mision (
  id_mision             INT AUTO_INCREMENT PRIMARY KEY,
  id_mundo              INT NOT NULL,
  id_franja             INT NOT NULL,
  titulo                VARCHAR(100) NOT NULL,
  objetivo_aprendizaje  VARCHAR(255) NOT NULL,
  orden                 TINYINT NOT NULL,
  leccion               TEXT NOT NULL,
  actividad_ludica      TEXT NOT NULL,
  UNIQUE (id_mundo, id_franja, orden),
  FOREIGN KEY (id_mundo)  REFERENCES mundo(id_mundo) ON DELETE CASCADE,
  FOREIGN KEY (id_franja) REFERENCES franja_etaria(id_franja)
);

CREATE TABLE pregunta (
  id_pregunta  INT AUTO_INCREMENT PRIMARY KEY,
  id_mision    INT NOT NULL,
  enunciado    VARCHAR(255) NOT NULL,
  orden        TINYINT NOT NULL,
  UNIQUE (id_mision, orden),
  FOREIGN KEY (id_mision) REFERENCES mision(id_mision) ON DELETE CASCADE
);

CREATE TABLE opcion_pregunta (
  id_opcion    INT AUTO_INCREMENT PRIMARY KEY,
  id_pregunta  INT NOT NULL,
  texto        VARCHAR(150) NOT NULL,
  es_correcta  BOOLEAN NOT NULL DEFAULT FALSE,
  FOREIGN KEY (id_pregunta) REFERENCES pregunta(id_pregunta) ON DELETE CASCADE
);

CREATE TABLE actividad_interactiva (
  id_actividad    INT AUTO_INCREMENT PRIMARY KEY,
  id_mision       INT NOT NULL,
  tipo            ENUM('ARRASTRAR_SOLTAR','UNIR_FLECHAS','COMPLETAR') NOT NULL,
  consigna        VARCHAR(255) NOT NULL,
  orden           TINYINT NOT NULL,
  contenido_json  JSON NOT NULL,
  UNIQUE (id_mision, orden),
  FOREIGN KEY (id_mision) REFERENCES mision(id_mision) ON DELETE CASCADE
);

CREATE TABLE alumno (
  id_alumno         INT AUTO_INCREMENT PRIMARY KEY,
  id_franja         INT NOT NULL,
  nombre            VARCHAR(50) NOT NULL,
  apellido          VARCHAR(50) NOT NULL,
  fecha_nacimiento  DATE NOT NULL,
  activo            BOOLEAN NOT NULL DEFAULT TRUE,
  FOREIGN KEY (id_franja) REFERENCES franja_etaria(id_franja)
);

CREATE TABLE usuario (
  id_usuario     INT AUTO_INCREMENT PRIMARY KEY,
  nombre         VARCHAR(80) NOT NULL,
  nombre_usuario VARCHAR(30) NOT NULL UNIQUE,
  clave_hash     CHAR(64) NOT NULL,
  rol            ENUM('DOCENTE','ADMINISTRADOR') NOT NULL
);

CREATE TABLE progreso (
  id_progreso              INT AUTO_INCREMENT PRIMARY KEY,
  id_alumno                INT NOT NULL,
  id_mision                INT NOT NULL,
  fecha_inicio             DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  fecha_finalizacion       DATETIME NULL,
  completada               BOOLEAN NOT NULL DEFAULT FALSE,
  resultado_cuestionario   TINYINT NULL CHECK (resultado_cuestionario BETWEEN 0 AND 3),
  UNIQUE (id_alumno, id_mision),
  FOREIGN KEY (id_alumno) REFERENCES alumno(id_alumno) ON DELETE CASCADE,
  FOREIGN KEY (id_mision) REFERENCES mision(id_mision) ON DELETE CASCADE
);

CREATE TABLE resultado_actividad (
  id_resultado  INT AUTO_INCREMENT PRIMARY KEY,
  id_progreso   INT NOT NULL,
  id_actividad  INT NOT NULL,
  correcta      BOOLEAN NOT NULL,
  intentos      TINYINT NOT NULL DEFAULT 1,
  fecha         DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  UNIQUE (id_progreso, id_actividad),
  FOREIGN KEY (id_progreso)  REFERENCES progreso(id_progreso) ON DELETE CASCADE,
  FOREIGN KEY (id_actividad) REFERENCES actividad_interactiva(id_actividad) ON DELETE CASCADE
);

CREATE INDEX idx_mision_mundo_franja ON mision(id_mundo, id_franja);
CREATE INDEX idx_progreso_alumno ON progreso(id_alumno);

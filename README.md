# Ruta del Programador — Prototipo (Seminario de Práctica de Informática, Entrega 2)

Sistema de escritorio para la enseñanza lúdica de fundamentos de programación (Coders Academy).
Prototipo operacional en Java 21 + MySQL 8.0 (JDBC), con arquitectura MVC y capa DAO.

## Estructura
- `sql/01_creacion.sql` — creación de la base y las tablas
- `sql/02_insercion.sql` — carga inicial de datos de prueba
- `sql/03_consultas.sql` — consultas
- `sql/04_borrado.sql` — actualización y borrado de registros
- `src/main/java/ar/codersacademy/ruta/` — modelo, dao, controlador, vista
- `diagramas/` — modelo de dominio, casos de uso, clases de diseño y DER

## Cómo ejecutarlo
1. Ejecutar los scripts `01` y `02` en MySQL (usuario `root`, puerto 3306).
2. Abrir el proyecto Maven en IntelliJ IDEA y ejecutar `vista.ConsolaApp`.

Autor: Jesús Luis Rodrigo Lobo — Universidad Siglo 21.

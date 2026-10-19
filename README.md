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

## Dependencias
Los `.jar` no se versionan. Desde la raíz del repo:
1. `./descargar-dependencias.sh` baja Jackson 2.17.2 (`databind`, `core`, `annotations`) a `lib/` y verifica su SHA-1.
2. Descargar `mysql-connector-j-8.4.0.jar` desde <https://dev.mysql.com/downloads/connector/j/> (o Maven Central,
   `com.mysql:mysql-connector-j:8.4.0`) y dejarlo en la raíz del repo.

## Cómo ejecutarlo
Requiere JDK 21 y MySQL 8.0 en `localhost:3306` (usuario y clave `root`).
1. Ejecutar los scripts `sql/01_creacion.sql` y `sql/02_insercion.sql` en MySQL.
2. Compilar y ejecutar desde la raíz del repo:
```bash
javac -cp "lib/*" -d out $(find src -name "*.java")
java -cp "out:mysql-connector-j-8.4.0.jar:lib/*" ar.codersacademy.ruta.vista.ConsolaApp
```
También se puede abrir como proyecto Maven en un IDE (el `pom.xml` declara las dependencias).

Autor: Jesús Luis Rodrigo Lobo — Universidad Siglo 21.

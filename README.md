# Ruta del Programador — Prototipo (Seminario de Práctica de Informática, Entrega 3)

Sistema de escritorio para la enseñanza lúdica de fundamentos de programación (Coders Academy).
Prototipo operacional en Java 21 + MySQL 8.0 (JDBC), con arquitectura MVC y capa DAO.

## Estructura
- `sql/01_creacion.sql` — creación de la base y las tablas
- `sql/02_insercion.sql` — carga inicial de datos de prueba
- `sql/03_consultas.sql` — consultas
- `sql/04_borrado.sql` — actualización y borrado de registros
- `sql/05_demo.sql` — deja a Tomás sin progreso en la Misión 1 (ver *Demostración*)
- `src/main/java/ar/codersacademy/ruta/` — modelo, dao, controlador, vista y util (ordenamiento y búsqueda)
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
## Uso
El programa abre un menú de consola. Se elige un perfil (opción 1), una Misión (2) y se puede ver la lección (3), resolver el
cuestionario (4: se aprueba con 2 de 3 y habilita las actividades) y las actividades interactivas (5). Una respuesta incorrecta
muestra una pista y la actividad vuelve a aparecer más adelante; escribir `salir` vuelve al menú conservando el progreso. La
Misión se completa al terminar sus actividades. La opción 6 muestra el progreso propio y la 7, el del docente (ordenado por
nombre o por Misiones completadas, y búsqueda por nombre). Solo la Misión 1 de la franja de 7 a 10 años tiene contenido cargado.

## Diagramas
Los `.puml` son la fuente y los `.png` se regeneran con PlantUML (requiere Graphviz). `plantuml.jar` va en `tools/`, fuera del
classpath del programa, y no se versiona:
```bash
mkdir -p tools
curl -fsSL -o tools/plantuml.jar https://github.com/plantuml/plantuml/releases/download/v1.2026.9/plantuml.jar
java -jar tools/plantuml.jar -tpng diagramas/clases_diseno.puml diagramas/secuencia_cuestionario.puml
```
`secuencia_cuestionario.puml` es el diagrama de secuencia de CU04 (Resolver cuestionario). `clases_diseno.puml` es el diagrama de clases del código (paquetes modelo, dao, controlador, vista y util); `dominio.puml` es el
modelo de dominio y no incluye clases de implementación.

## Demostración
Con los datos iniciales, Tomás ya tiene aprobado el cuestionario de la Misión 1, así que el programa lo lleva directo a las
actividades. Para recorrer el flujo completo (cuestionario → actividades → Misión completada), ejecutar después de `01` y `02`:
```bash
mysql -uroot -proot < sql/05_demo.sql
```
Para volver al estado inicial, correr `01` y `02` otra vez (`01` borra y recrea la base). No usar `sql/04_borrado.sql` para esto.

También se puede abrir como proyecto Maven en un IDE (el `pom.xml` declara las dependencias).

Autor: Jesús Luis Rodrigo Lobo — Universidad Siglo 21.

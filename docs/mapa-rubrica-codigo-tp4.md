# Mapa rúbrica → código (Entrega 4, final)

Dónde cumple el prototipo cada característica de la consigna del TP4 (`docs/consigna-tp4.md`). Las rutas son relativas a
`src/main/java/ar/codersacademy/ruta/` y los números de línea corresponden al código de este commit. El mapa de la
Entrega 3 (`docs/mapa-rubrica-codigo.md`) conserva los números de línea de aquella entrega.

**Compilar y ejecutar** (desde la raíz del repo; ver `README.md` para dependencias y base de datos):
```bash
javac -cp "lib/*" -d out $(find src -name "*.java")
java -cp "out:mysql-connector-j-8.4.0.jar:lib/*" ar.codersacademy.ruta.vista.ConsolaApp
```

## 1. Corrección de las observaciones anteriores
- **Entrega 3:** el cuestionario se aprueba con 2 de 3 y no completa la Misión; el promedio de un alumno sin cuestionarios se muestra `-` (ver el mapa de la Entrega 3).
- **TP2, corrección 6** (reemplazar los wireframes de las Figuras 7.1 a 7.3 por prototipos propios): se resuelve en el documento PDF, con capturas de este prototipo. No hay cambio de código.

## 2. Patrón de diseño: MVC + DAO con JDBC
- **Vista** (`vista/ConsolaApp.java:30`): única clase con `Scanner` y `System.out`.
- **Controlador** (`controlador/RutaController.java:32`): reglas de negocio; coordina la vista y los DAO.
- **Modelo** (`modelo/`): entidades, sin acceso a la base ni impresión.
- **DAO** (`dao/`): todo el SQL, con `PreparedStatement` y try-with-resources. Es el **paquete dedicado a la persistencia en MySQL** que pide la consigna.
- **Inyección de dependencias**: el controlador recibe las cinco interfaces DAO por constructor (`RutaController.java:61`) y la vista arma el cableado con las implementaciones JDBC (`ConsolaApp.java:32-33`).
- Factory dentro del DAO para elegir la subclase de actividad: `dao/ActividadDAOJdbc.java:47-50` (`crearActividad`).
- Diagrama: `diagramas/clases_diseno.puml` (y `.png`).

## 3. Persistencia y consulta en MySQL
- **Conexión**: `dao/ConexionBD.java:15-16` (`DriverManager.getConnection`), único punto de conexión.
- **Consultas** (`SELECT`): `dao/AlumnoDAOJdbc.java:15`, `dao/MisionDAOJdbc.java:13` y `:36`, `dao/PreguntaDAOJdbc.java:14`, `dao/ActividadDAOJdbc.java:18`, `dao/ProgresoDAOJdbc.java:36` (nota), `:80` (actividades correctas), `:119` (detalle por Misión con subconsultas) y `:150-156` (resúmenes con `AVG` y `GROUP BY`).
- **Actualización de registros** (`INSERT ... ON DUPLICATE KEY UPDATE` y `UPDATE`): `dao/ProgresoDAOJdbc.java:18` (nota del cuestionario, conserva la mejor), `:59` (intento de actividad) y `:103` (Misión completada).
- **Resultados en la interfaz**: la vista imprime las listas y los resúmenes (`vista/ConsolaApp.java:90` perfiles, `:101` Misiones, `:202` progreso propio, `:242` progreso de los alumnos).

## 4. Excepciones en la interacción con MySQL
- **Excepción propia** `PersistenciaException` (`extends Exception`, con constructor con causa): `modelo/PersistenciaException.java:8-15`. Envuelve la `SQLException` original para que el controlador y la vista no dependan de `java.sql`.
- **Cada DAO captura `SQLException` y la relanza** con un mensaje en español: `catch (SQLException e)` + `throw new PersistenciaException(..., e)` en `dao/AlumnoDAOJdbc.java:24-25`, `dao/MisionDAOJdbc.java:28, 44`, `dao/PreguntaDAOJdbc.java:48`, `dao/ActividadDAOJdbc.java:40` y `dao/ProgresoDAOJdbc.java:29, 49, 73, 95, 112, 142, 184`.
- **Qué ve el usuario con MySQL apagado**: la vista captura `PersistenciaException` y muestra solo el mensaje (`vista/ConsolaApp.java:63`), sin stack trace; el programa vuelve al menú. Ejemplo: `No se pudo obtener la lista de alumnos`.
- **Se conserva el detalle técnico** en la causa (`getCause()`), pensada para depurar y no para el alumno.
- **Estado consistente ante una falla**: `RutaController.responder` saca la actividad de la cola (`poll`, `:174, 179, 182`) recién después de guardar el intento (`peek`, `:170`); si la base falla, la actividad sigue pendiente.
- **try-with-resources** en todo acceso a la base: `dao/ProgresoDAOJdbc.java:23`, `dao/ActividadDAOJdbc.java:25`, `dao/PreguntaDAOJdbc.java:21`.
- Otras excepciones: `ContenidoInvalidoException` (`modelo/ContenidoInvalidoException.java:4`, capturada en `vista/ConsolaApp.java:61` y `:121`), `IOException` al exportar (`vista/ConsolaApp.java:65`), `InputMismatchException` (`vista/ConsolaApp.java:273-282`).

## 5. Clases abstractas e interfaces
- **Interfaces DAO** (el contrato; el controlador solo las conoce): `dao/AlumnoDAO.java:10`, `dao/MisionDAO.java:8`, `dao/PreguntaDAO.java:9`, `dao/ActividadDAO.java:9`, `dao/ProgresoDAO.java:9`.
- **Implementaciones JDBC** (`implements` + `@Override`): `dao/AlumnoDAOJdbc.java:12`, `dao/MisionDAOJdbc.java:10`, `dao/PreguntaDAOJdbc.java:11`, `dao/ActividadDAOJdbc.java:13`, `dao/ProgresoDAOJdbc.java:12`.
- **Qué se gana**: el controlador depende del contrato y no de JDBC (`RutaController.java:54-58`); se puede cambiar la implementación (p. ej. una en memoria para pruebas) sin tocar las reglas de negocio.
- **Clase abstracta** `modelo/ActividadInteractiva.java:9`, con tres métodos abstractos y tres subclases (`ActividadOrdenar`, `ActividadUnir`, `ActividadCompletar`): ver el mapa de la Entrega 3.

## 6. Arreglos y ArrayList, de forma complementaria
- **Arreglo `String[]` para lo fijo**: los encabezados del CSV son siempre 5 columnas: `util/ExportadorCSV.java:24` (`ENCABEZADOS`).
- **`ArrayList` para lo variable**: la cantidad de Misiones del alumno no se conoce de antemano: `util/ExportadorCSV.java:33-34` (`ArrayList<String[]> armarFilas`).
- **Cada fila es un arreglo** del mismo largo que los encabezados: `util/ExportadorCSV.java:36` (`String[] fila = {...}`). Además `escribirLinea` arma un `String[] escapadas` del tamaño de la fila (`:61`).
- **Uso conjunto al escribir**: `escribir(Path, String[] encabezados, List<String[]> filas)` recibe ambos (`:49-53`).
- Otros arreglos: los que devuelve `split` y el `boolean[] unido` en la validación (`modelo/ActividadUnir.java:72-76`, `modelo/ActividadOrdenar.java:66`).
- **`ArrayList`** como lista de resultados en todos los DAO (`dao/ProgresoDAOJdbc.java:130`) y `List` en el modelo (`modelo/Mision.java:17-18`); además `Queue`/`ArrayDeque` para las actividades pendientes (`controlador/RutaController.java:71`).

## 7. Archivos (opcional recomendado)
- **Exportar el progreso de un alumno a CSV**, una fila por Misión, separador `;` y UTF-8 con BOM para Excel:
  - Menú: opción 8 (`vista/ConsolaApp.java:85` el texto, `:212-217` la acción).
  - Controlador: `RutaController.exportarProgreso` (`:219-224`) crea `exportaciones/` y llama al exportador.
  - Consulta: `ProgresoDAO.detallePorMision` (`dao/ProgresoDAO.java:32`) y su implementación (`dao/ProgresoDAOJdbc.java:119`), que devuelve `DetalleMision` (`modelo/DetalleMision.java:9`).
  - Escritura: `util/ExportadorCSV.java:48-55` (`Files.newBufferedWriter` en try-with-resources, BOM `﻿` en `:21`) y escape de celdas en `:70`.
- La salida queda en `exportaciones/progreso-alumno-{id}.csv` (carpeta fuera del repo).

## Mejora futura
- La conexión (URL, usuario y clave) está fija en `dao/ConexionBD.java:9-11`. Queda como mejora leerla de un `config.properties` ignorado por Git.

## Para mostrar en el video
1. Menú → perfil de Tomás → opción 6 (progreso) → opción 8 (exportar) y mostrar `exportaciones/progreso-alumno-2.csv`.
2. Opción 7: ordenado por nombre y búsqueda de "tomas gomez".
3. Con MySQL apagado (`sudo systemctl stop mysql`): opción 1 y ver el mensaje `No se pudo obtener la lista de alumnos` sin stack trace; volver a prenderlo y repetir.
4. Mostrar en el código `PersistenciaException`, un `catch (SQLException)` de un DAO, una interfaz DAO y `ExportadorCSV`.

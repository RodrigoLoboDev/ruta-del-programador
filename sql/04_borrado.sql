-- Ruta del Programador - Actualización y borrado de registros
USE ruta_programador;

-- B1. Baja lógica de un alumno (conserva su historial)
UPDATE alumno SET activo = FALSE WHERE id_alumno = 2;

-- B2. Borrado físico de una opción mal cargada
DELETE FROM opcion_pregunta WHERE id_pregunta = 2 AND texto = 'No pasa nada';

-- B3. Borrado físico de un alumno: por ON DELETE CASCADE se eliminan también su progreso y resultados
DELETE FROM alumno WHERE id_alumno = 3;

-- Verificación
SELECT id_alumno, nombre, activo FROM alumno;
SELECT COUNT(*) AS opciones_pregunta_2 FROM opcion_pregunta WHERE id_pregunta = 2;

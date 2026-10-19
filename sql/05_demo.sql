-- Ruta del Programador - Estado de demostración (capturas del TP3)
-- Deja a Tomás sin progreso en la Misión 1 (sin cuestionario ni actividades) para recorrer el flujo completo:
-- cuestionario -> actividades -> Misión completada. Los resultados de actividad se borran en cascada.
-- No borra la base ni a otros alumnos; se corre después de 01 y 02 y se puede repetir.
USE ruta_programador;

DELETE FROM progreso
WHERE id_mision = 1
  AND id_alumno = (SELECT id_alumno FROM alumno WHERE nombre = 'Tomás' AND apellido = 'Gómez');

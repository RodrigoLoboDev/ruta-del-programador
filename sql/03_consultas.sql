-- Ruta del Programador - Consultas
USE ruta_programador;

-- Q1. Mundos y Misiones disponibles para la franja 7 a 10 años (RF02)
SELECT m.orden AS nro_mundo, m.nombre AS mundo, mi.orden AS nro_mision, mi.titulo
FROM mundo m
JOIN mision mi ON mi.id_mundo = m.id_mundo
JOIN franja_etaria f ON f.id_franja = mi.id_franja
WHERE f.nombre = '7 a 10 años'
ORDER BY m.orden, mi.orden;

-- Q2. Cuestionario de una Misión con sus opciones (RF05)
SELECT p.orden, p.enunciado, o.texto, o.es_correcta
FROM pregunta p
JOIN opcion_pregunta o ON o.id_pregunta = p.id_pregunta
WHERE p.id_mision = 1
ORDER BY p.orden;

-- Q3. Progreso de cada alumno: Misiones completadas y promedio del cuestionario (RF08, RF09)
SELECT a.nombre, a.apellido,
       COUNT(CASE WHEN pr.completada THEN 1 END) AS misiones_completadas,
       ROUND(AVG(pr.resultado_cuestionario), 2) AS promedio_cuestionario
FROM alumno a
LEFT JOIN progreso pr ON pr.id_alumno = a.id_alumno
GROUP BY a.id_alumno, a.nombre, a.apellido
ORDER BY misiones_completadas DESC;

-- Q4. Actividades con más de un intento (alumnos que necesitan refuerzo) (RF09)
SELECT a.nombre, a.apellido, mi.titulo AS mision, ai.consigna, ra.intentos, ra.correcta
FROM resultado_actividad ra
JOIN progreso pr ON pr.id_progreso = ra.id_progreso
JOIN alumno a ON a.id_alumno = pr.id_alumno
JOIN mision mi ON mi.id_mision = pr.id_mision
JOIN actividad_interactiva ai ON ai.id_actividad = ra.id_actividad
WHERE ra.intentos > 1 OR ra.correcta = FALSE;

-- Q5. Validación de acceso de un usuario (RF10)
SELECT id_usuario, nombre, rol
FROM usuario
WHERE nombre_usuario = 'admin' AND clave_hash = SHA2('admin123', 256);

-- Q6. Leer un dato del JSON de una actividad (RNF08)
SELECT consigna, JSON_EXTRACT(contenido_json, '$.respuesta') AS respuesta
FROM actividad_interactiva
WHERE tipo = 'COMPLETAR';

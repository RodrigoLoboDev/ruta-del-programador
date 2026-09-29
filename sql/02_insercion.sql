-- Ruta del Programador - Carga inicial de datos de prueba
USE ruta_programador;

INSERT INTO franja_etaria (nombre, edad_minima, edad_maxima) VALUES
 ('7 a 10 años', 7, 10),
 ('11 a 14 años', 11, 14);

INSERT INTO mundo (nombre, concepto, orden) VALUES
 ('Algoritmos',   'Secuencia de pasos para resolver una tarea', 1),
 ('Secuencias',   'El orden de los pasos cambia el resultado', 2),
 ('Patrones',     'Reconocer lo que se repite', 3),
 ('Bucles',       'Repetir pasos sin reescribirlos', 4),
 ('Eventos',      'Acciones que ocurren cuando algo sucede', 5),
 ('Condicionales','Tomar decisiones según una condición', 6),
 ('Variables',    'Guardar datos que pueden cambiar', 7),
 ('Debugging',    'Encontrar y corregir errores', 8);

INSERT INTO mision (id_mundo, id_franja, titulo, objetivo_aprendizaje, orden, leccion, actividad_ludica) VALUES
 (1, 1, 'Pasos para cepillarse los dientes', 'Comprender que un algoritmo es una secuencia de pasos',
  1, 'Un algoritmo es una lista de pasos ordenados. Cepillarse los dientes tiene pasos: tomar el cepillo, poner pasta, cepillar y enjuagar.',
  'En grupo, un alumno da instrucciones a otro para armar un sándwich y el resto verifica si faltan pasos.'),
 (1, 1, 'Muchos caminos, una meta', 'Reconocer que existen varios algoritmos válidos para una tarea',
  2, 'Para llegar a la escuela se pueden tomar distintos caminos. Todos son algoritmos válidos.',
  'Cada grupo dibuja un camino distinto para llegar al mismo punto del aula.'),
 (1, 2, 'Algoritmos en la vida diaria', 'Formalizar un algoritmo como una secuencia finita y ordenada',
  1, 'Un algoritmo es finito, ordenado y preciso. Ejemplo: la receta de una torta.',
  'Escribir el algoritmo para preparar un mate y probarlo siguiendo las instrucciones al pie de la letra.'),
 (6, 1, '¿Llevo paraguas?', 'Entender que un condicional decide según una condición',
  1, 'SI llueve, ENTONCES llevo paraguas. Si no llueve, no lo llevo.',
  'Juego de Simón dice con condiciones: si la tarjeta es roja, saltar.');

INSERT INTO pregunta (id_mision, enunciado, orden) VALUES
 (1, '¿Qué es un algoritmo?', 1),
 (1, '¿Qué pasa si cambiamos el orden de los pasos?', 2),
 (1, '¿Cuál es el primer paso para cepillarse?', 3);

INSERT INTO opcion_pregunta (id_pregunta, texto, es_correcta) VALUES
 (1, 'Una lista de pasos ordenados', TRUE),
 (1, 'Un tipo de computadora', FALSE),
 (1, 'Un juego', FALSE),
 (2, 'El resultado puede cambiar', TRUE),
 (2, 'No pasa nada', FALSE),
 (3, 'Tomar el cepillo', TRUE),
 (3, 'Enjuagar', FALSE);

INSERT INTO actividad_interactiva (id_mision, tipo, consigna, orden, contenido_json) VALUES
 (1, 'ARRASTRAR_SOLTAR', 'Ordená los pasos para cepillarte los dientes', 1,
  '{"elementos":["Tomar el cepillo","Poner pasta","Cepillar","Enjuagar"],"ordenCorrecto":[0,1,2,3]}'),
 (1, 'UNIR_FLECHAS', 'Uní cada paso con su dibujo', 2,
  '{"pares":[["Poner pasta","pasta.png"],["Enjuagar","vaso.png"]]}'),
 (1, 'COMPLETAR', 'Completá la definición', 3,
  '{"texto":"Un algoritmo es una ___ de pasos","opciones":["secuencia","pelota"],"respuesta":"secuencia"}');

INSERT INTO alumno (id_franja, nombre, apellido, fecha_nacimiento) VALUES
 (1, 'Martina', 'Pérez', '2017-05-12'),
 (1, 'Tomás',   'Gómez', '2018-02-03'),
 (2, 'Lucía',   'Díaz',  '2013-09-21');

-- Claves de prueba: "docente123" y "admin123" (SHA-256)
INSERT INTO usuario (nombre, nombre_usuario, clave_hash, rol) VALUES
 ('Docente de prueba', 'docente', SHA2('docente123', 256), 'DOCENTE'),
 ('Lehila Gosne',      'admin',   SHA2('admin123', 256),   'ADMINISTRADOR');

INSERT INTO progreso (id_alumno, id_mision, fecha_finalizacion, completada, resultado_cuestionario) VALUES
 (1, 1, NOW(), TRUE, 3),
 (1, 2, NULL, FALSE, NULL),
 (2, 1, NOW(), TRUE, 2);

INSERT INTO resultado_actividad (id_progreso, id_actividad, correcta, intentos) VALUES
 (1, 1, TRUE, 1),
 (1, 2, TRUE, 2),
 (1, 3, TRUE, 1),
 (3, 1, FALSE, 3);

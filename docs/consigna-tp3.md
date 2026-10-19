# Consigna Entrega 3 — resumen

**Vence:** lunes 19/10/2026, 23:59. Seminario de Práctica de Informática, Universidad Siglo 21.

## Qué se pide
Prototipo operacional en Java (puede incluir solo algunas características del sistema final) y un documento que explique
qué sección del código cumple cada característica. El caso guía de la cátedra (ECOViento) es de **consola**
(`Scanner` + `while` + `switch`); no se pide JavaFX.

## Características obligatorias (45 pts, por escalones; con hasta el 60% se cae a 25)
1. Sintaxis, tipos de datos y estructuras de control correctos.
2. Tratamiento y manejo de excepciones.
3. Encapsulamiento, herencia, polimorfismo y abstracción.
4. Menú de selección.
5. Estructuras condicionales y repetitivas.
6. Declaración y creación de objetos.
7. Constructores para inicializar objetos.
8. Opcional: algoritmos de ordenación y búsqueda; estructuras como pilas, colas, listas.

## Rúbrica restante
- El código compila y se ejecuta correctamente: **20 pts**.
- El documento explica qué sección del código cumple cada característica: **20 pts**.
- El núcleo de los casos de uso tiene que estar sí o sí; coherencia entre diseño (TP2) y código.

## Diseño del TP2 (corregido, 90/100)
Casos de uso núcleo: CU01 Seleccionar perfil · CU02 Ver Mundos y Misiones · CU03 Ver lección · CU04 Resolver cuestionario ·
CU05 Resolver actividad interactiva · CU06 Registrar progreso · CU07 Ver pista (extend de CU05 ante respuesta incorrecta) ·
CU08 Ver progreso propio · CU09 Ver progreso de alumnos.

Reglas: el cuestionario (3 preguntas) se aprueba con 2 de 3 y **habilita** las actividades; la Misión se **completa** al
terminar sus actividades (3 a 5 por Misión). La propuesta técnica declara Jackson para leer `contenido_json`.
Devolución del profesor: las entidades no se gestionan a sí mismas (no acceden a la base ni imprimen).

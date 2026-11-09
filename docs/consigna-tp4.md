# TP4 — Seminario de Práctica de Informática (entrega final)

**Vence:** lun 9-nov-2026 23:59 · 100 pts · intentos ilimitados · individual · entrega incremental.

## Enunciado
Retomar el proyecto y realizar la versión final integradora. Elegir un patrón de diseño coherente con la arquitectura ya definida. El prototipo debe interactuar con MySQL: establecer conexiones, realizar consultas, actualizar registros y presentar resultados en la interfaz. Usar clases abstractas o interfaces para favorecer la reutilización de métodos, con una estructura robusta de gestión de excepciones. Aplicar la declaración, inicialización y manipulación de arreglos, y usar la clase ArrayList.

## Características obligatorias del prototipo
1. Corrección de las observaciones de las entregas anteriores (se evalúa el proyecto en su totalidad).
2. Selección del patrón de diseño y justificación (MVC + DAO con JDBC).
3. Persistencia y consulta en MySQL: conexión, consultas, actualización de registros y resultados en la interfaz.
4. Correcta aplicación de excepciones en la interacción con MySQL.
5. Inclusión pertinente de clases abstractas o interfaces.
6. Utilización complementaria de arreglos y de ArrayList.
- Debe haber un paquete de clases dedicado a la persistencia en MySQL.
- Opcional, pero recomendado para el EFIP I: archivos para guardar o recuperar información (por ejemplo, exportar un output).

## Entregables
- Documento PDF `LOBO-RODRIGO-AP4.pdf` (A4, Calibri 11, espaciado simple, con portada), incremental, con la explicación del código por característica, los diagramas explicados y el enlace a GitHub.
- Proyecto completo en un repo público de GitHub: código, base de datos y video.
- Video de unos 3 minutos: algunas partes del código, la ejecución y lo que se ve en pantalla, narrado con voz.

## Rúbrica (100 pts)
| Criterio | Pts |
|---|---|
| Código completo que compila y se ejecuta | 10 |
| El prototipo cumple las características solicitadas | 45 |
| Documento que explica las secciones del código para cada característica | 15 |
| Formato, video de 3 minutos y correcciones de entregas anteriores | 15 |
| Participación (foros, encuentros, aportes, comunicación) | 15 |

## Decisiones tomadas
- Interfaz por **consola, sin JavaFX**: el profesor recomendó mantener la estructura si no se venía trabajando con interfaz gráfica.
- Corrección pendiente del TP2 (la número 6): reemplazar los wireframes de las Figuras 7.1 a 7.3 por prototipos propios.

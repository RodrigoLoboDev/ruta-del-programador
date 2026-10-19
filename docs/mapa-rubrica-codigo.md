# Mapa rúbrica → código (Entrega 3)

Dónde cumple el prototipo cada característica de la consigna. Las rutas son relativas a
`src/main/java/ar/codersacademy/ruta/` y los números de línea corresponden al código entregado.

**Compilar y ejecutar** (desde la raíz del repo; ver `README.md` para dependencias y base de datos):
```bash
javac -cp "lib/*" -d out $(find src -name "*.java")
java -cp "out:mysql-connector-j-8.4.0.jar:lib/*" ar.codersacademy.ruta.vista.ConsolaApp
```

## 1. Sintaxis, tipos de datos y estructuras de control
- Tipos primitivos (`int`, `boolean`), `String`, `Double` (que admite `null`), colecciones genéricas (`List<Alumno>`, `Queue<ActividadInteractiva>`) y un `record`:
  `controlador/RutaController.java:28-36, 55`, `modelo/ResumenProgreso.java:4`.
- Estructuras de control en la vista: `while` del menú (`vista/ConsolaApp.java:37`), `switch` de opciones (`:41`), `for` y `for-each` para listar (`:79`, `:94`, `:135`).
- Estructuras de control en las reglas de negocio: `if` y `for` en `RutaController.rendirCuestionario` (`:87-102`) y `apruebaCuestionario` (`:76`).

## 2. Tratamiento y manejo de excepciones
- **Excepción propia** `ContenidoInvalidoException` (`extends Exception`): `modelo/ContenidoInvalidoException.java:4`.
  - Se lanza ante una Misión sin lección, con otra cantidad de preguntas o con menos de 3 / más de 5 actividades: `modelo/Mision.java:29-41`.
  - Se lanza ante un JSON inválido, un campo que falta o un tipo desconocido: `dao/ActividadDAO.java:31-33, 46-64`.
  - Se lanza ante una pregunta mal cargada: `dao/PreguntaDAO.java:49-55`.
- **Captura y mensaje amable** en la vista: `vista/ConsolaApp.java:51` (menú) y `:108` (al elegir una Misión). El chico ve "Esta Misión todavía no está lista, ¡probá con otra!" y el programa sigue.
- **`SQLException`** capturada en el menú sin cortar el programa: `vista/ConsolaApp.java:53`.
- **`InputMismatchException`** en la lectura numérica, con limpieza del buffer: `vista/ConsolaApp.java:250-264` (`leerEntero`).
- **Fin de la entrada** (`NoSuchElementException`): `vista/ConsolaApp.java:57`.
- **Validaciones con excepciones estándar**: `IllegalArgumentException` en constructores y setters (`modelo/Alumno.java:22-33`, `modelo/Pregunta.java:13-22`) e `IllegalStateException` si se intenta una actividad sin el cuestionario aprobado (`controlador/RutaController.java:167-170`).
- **try-with-resources** en todo acceso a la base: `dao/ProgresoDAO.java:18`, `dao/ActividadDAO.java:23`, `dao/PreguntaDAO.java:18`.
- **Respuesta mal formada**: `NumberFormatException` capturada y tratada como respuesta incorrecta (`modelo/ActividadOrdenar.java:74`, `modelo/ActividadUnir.java:85`, `modelo/ActividadCompletar.java:46`).

## 3. Encapsulamiento, herencia, polimorfismo y abstracción
- **Encapsulamiento**: atributos `private`, id `final` sin setter, setters que validan y colecciones inmutables (`List.copyOf`).
  - `modelo/Alumno.java:5-8` (atributos), `:22-33` (setters con validación).
  - `modelo/Mision.java:11-18`, `setContenido` en `:29` (rechaza contenido inválido antes de asignarlo).
  - `modelo/Pregunta.java:7-10, 25`.
- **Abstracción**: `modelo/ActividadInteractiva.java:9` es una clase `abstract` con tres métodos abstractos que definen qué es una actividad sin decir cómo se resuelve: `getInstrucciones()` (`:28`), `getElementos()` (`:31`) y `validar(String)` (`:34`).
- **Herencia**: tres subclases con `extends` y `super(...)`.
  - `modelo/ActividadOrdenar.java:9` (tipo ARRASTRAR_SOLTAR), `modelo/ActividadUnir.java:9` (UNIR_FLECHAS) y `modelo/ActividadCompletar.java:7` (COMPLETAR).
  - Cada constructor llama al de la clase base (`:24`, `:23`, `:15`).
- **Polimorfismo**: cada subclase sobrescribe (`@Override`) los tres métodos y quien las usa solo conoce la clase base.
  - Implementaciones de `validar`: `modelo/ActividadOrdenar.java:62`, `modelo/ActividadUnir.java:68`, `modelo/ActividadCompletar.java:41`.
  - Uso polimórfico en la vista, sin `switch` por tipo: `vista/ConsolaApp.java:172` (`getInstrucciones`), `:173` (`getElementos`).
  - Uso polimórfico en el controlador: `controlador/RutaController.java:154` (`actividad.validar(respuesta)`).
  - Único lugar que conoce las clases concretas, un **Factory**: `dao/ActividadDAO.java:46` (`crearActividad`).
- **Capas (MVC + DAO)**: el modelo no accede a la base ni imprime; el SQL vive solo en `dao/`; la única clase con `Scanner` y `System.out` es `vista/ConsolaApp.java`.

## 4. Menú de selección
- Menú principal con 8 opciones, mostrado en `vista/ConsolaApp.java:62` (`mostrarMenu`) y atendido por el `switch` de `:41-49`.
- Submenú del docente (opciones 1 a 3 y volver): `vista/ConsolaApp.java:199-218`.
- Cada opción corresponde a un caso de uso (ver la tabla del final).

## 5. Estructuras condicionales y repetitivas
- Repetitivas: `while` del menú (`vista/ConsolaApp.java:37`), del submenú (`:201`), de lectura de números hasta que sean válidos (`:251`) y de actividades pendientes (`:170`); `for` y `for-each` en `:79`, `:94`, `:135`, `:138`, `:173`.
- Condicionales: `if` / `else` y `switch` sobre el resultado de responder una actividad (`vista/ConsolaApp.java:178`); reglas de negocio en `controlador/RutaController.java:76-81` (aprobación), `:148-151` ("salir"), `:156-164` (respuesta correcta o incorrecta).
- Repetitivas con condición al final: `do { ... } while` para mezclar sin mostrar el orden resuelto (`modelo/ActividadOrdenar.java:40-42`, `modelo/ActividadUnir.java:44-46`).

## 6. Declaración y creación de objetos
- Creación con `new` de entidades desde la base: `dao/AlumnoDAO.java` (`new Alumno(...)`), `dao/MisionDAO.java` (`new Mision(...)`), `dao/PreguntaDAO.java:49-52` (`new Pregunta(...)`).
- Creación de subclases según el tipo (Factory): `dao/ActividadDAO.java:51-58`.
- Objetos colaboradores del controlador: `controlador/RutaController.java:48-52` (los cinco DAO) y `:55` (`new ArrayDeque<>()`).
- Objetos de la vista: `vista/ConsolaApp.java:24-25` (controlador y `Scanner`).

## 7. Constructores para inicializar objetos
- Constructor completo con validación: `modelo/Alumno.java:10` (usa los setters), `modelo/Mision.java:20`, `modelo/Pregunta.java:13`.
- Constructor protegido de la clase abstracta, que las subclases invocan con `super`: `modelo/ActividadInteractiva.java:17`.
- Constructores de las subclases, que validan y arman su estado inicial (incluido el orden mezclado): `modelo/ActividadOrdenar.java:15` y `:21`, `modelo/ActividadUnir.java:15` y `:21`, `modelo/ActividadCompletar.java:12`.
- **Sobrecarga de constructores**: el segundo recibe un `Random` para repetir el orden con una semilla en pruebas (`ActividadOrdenar.java:21`, `ActividadUnir.java:21`).
- Constructor de la excepción propia con y sin causa: `modelo/ContenidoInvalidoException.java:5, 9`.
- Constructor privado de las clases utilitarias: `util/Algoritmos.java:8`, `dao/ConexionBD.java:13`.

## 8. Algoritmos de ordenación y búsqueda; pilas, colas y listas
- **Ordenamiento por inserción escrito a mano**, genérico y estable: `util/Algoritmos.java:14-26`.
- **Búsqueda binaria escrita a mano** sobre la lista ordenada: `util/Algoritmos.java:31-45`.
- **Uso**: `controlador/RutaController.java:174` (`progresoPorNombre`), `:181` (`progresoPorMisiones`) y `:188` (`buscarAlumno`, que ordena por nombre y después busca). La búsqueda ignora mayúsculas y tildes (`:39-41`). En la vista: `vista/ConsolaApp.java:199-218`.
- **Cola (`Queue`)**: `controlador/RutaController.java:55` declara `Queue<ActividadInteractiva> pendientes = new ArrayDeque<>()`.
  - Se llena con las actividades aún no resueltas (`:120-123`), se mira la actual (`:136`) y se saca la que se responde (`:153`).
  - Una respuesta incorrecta la devuelve al final (`:157`) y escribir `salir` la vacía (`:149`).
- **Listas** (`List` / `ArrayList`) en todo el código: `controlador/RutaController.java:174`, `modelo/Mision.java:17-18`, `dao/PreguntaDAO.java`.

## Casos de uso del TP2 → código
| Caso de uso | Menú | Vista | Controlador | DAO |
|---|---|---|---|---|
| CU01 Seleccionar perfil | 1 | `ConsolaApp.java:77` | `alumnos` (`:59`) | `AlumnoDAO.listarActivos` |
| CU02 Ver Mundos y Misiones | 2 | `ConsolaApp.java:88` | `misionesDe` (`:61`) | `MisionDAO.listarPorFranja` |
| CU03 Ver lección | 3 | `ConsolaApp.java:114` | `cargarContenido` (`:66`) | `MisionDAO.obtenerLeccion` |
| CU04 Resolver cuestionario | 4 | `ConsolaApp.java:123` | `rendirCuestionario` (`:87`) | `PreguntaDAO.listarPorMision`, `ProgresoDAO.registrarCuestionario` |
| CU05 Resolver actividad interactiva | 5 | `ConsolaApp.java:154` | `iniciarActividades` (`:116`), `responder` (`:144`) | `ActividadDAO.listarPorMision` |
| CU06 Registrar progreso | (automático) | — | `responder` (`:144`) | `ProgresoDAO.registrarActividad` (`:48`), `marcarCompletada` (`:86`) |
| CU07 Ver pista | (tras un error) | `ConsolaApp.java:180-181` | `responder` → `INCORRECTA` | `ActividadInteractiva.getPista` (`:41`) |
| CU08 Ver progreso propio | 6 | `ConsolaApp.java:189` | `progresoDe` (`:196`) | `ProgresoDAO.resumenDe` (`:104`) |
| CU09 Ver progreso de alumnos | 7 | `ConsolaApp.java:199` | `progresoPorNombre`, `progresoPorMisiones`, `buscarAlumno` | `ProgresoDAO.resumenPorAlumno` (`:99`) |

## Reglas de negocio y dónde se aplican
- El cuestionario tiene 3 preguntas y se aprueba con 2 de 3: `controlador/RutaController.java:28-29, 76-81`; las 3 preguntas, además, se exigen en `modelo/Mision.java:34-37`.
- Aprobarlo **habilita** las actividades y **no** completa la Misión: `RutaController.cuestionarioAprobado` (`:106`) y `exigirCuestionarioAprobado` (`:167`), que se consulta antes de `registrarActividad` y de `marcarCompletada`.
- La Misión se **completa** al terminar sus actividades (3 a 5): `RutaController.responder` (`:160-162`) y `modelo/Mision.java:38-41`.
- Re-rendir el cuestionario conserva la mejor nota: `dao/ProgresoDAO.java:17` (`GREATEST`).
- Franja etaria: el alumno solo ve las Misiones de su franja: `dao/MisionDAO.java:10` (`WHERE id_franja = ?`).
- Un alumno sin cuestionarios muestra `-` y no `0,00`: `dao/ProgresoDAO.java:124-129` y `vista/ConsolaApp.java:226`.

## Para mostrar en la demostración
1. `sql/01` y `sql/02` y después `sql/05_demo.sql` (deja a Tomás sin progreso en la Misión 1).
2. Menú: perfil de Tomás → Misión 1 → lección → cuestionario → actividades (con un error y su pista) → Misión completada → progreso.
3. Excepciones: ingresar texto en vez de un número, o elegir "¿Llevo paraguas?" (sin contenido cargado).
4. Orden y búsqueda: opción 7 → ordenado por nombre / por Misiones completadas → buscar "tomas gomez".

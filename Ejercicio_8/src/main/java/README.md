# 📝 Ejercicio 8: Gestión de Alumnos (Mini Proyecto Integrador)

Este repositorio contiene mi solución para el **primer mini proyecto integrador del bloque de texto**. Se trata de una aplicación completa de consola en Java que gestiona un registro de estudiantes en un fichero `alumnos.txt`.

Partiendo del ejemplo guiado propuesto en clase, he realizado una **versión ampliada** que incorpora un menú interactivo, validaciones de seguridad y el uso de `java.nio.file`.

## 🚀 Operaciones Implementadas

El programa cumple con todos los requisitos del enunciado mediante el siguiente menú de opciones:

* **1. Alta**: Añade un nuevo alumno al final del fichero (`alumnos.txt`), garantizando que no se sobrescriban los datos anteriores.
* **2. Mostrar**: Lee el archivo completo y lista por pantalla a todos los alumnos registrados.
* **3. Buscar**: Localiza a un alumno por su nombre. Verifica previamente si el archivo existe y muestra tanto el número de coincidencias como la línea exacta donde se encuentran.
* **4. Contar**: Recorre el fichero y calcula el total exacto de registros actuales.
* **5. Salir**: Finaliza la ejecución del programa de forma controlada.

## 🛠️ Decisiones Técnicas y Herramientas (Java NIO)

Para esta implementación he priorizado el control de errores (usando bloques `try-catch`) y el rendimiento. Aquí detallo cómo se ha resuelto cada apartado:

| API / Método | Uso en la aplicación | Justificación técnica |
| :--- | :--- | :--- |
| `Paths.get("alumnos.txt")` | Declaración de la ruta | Usa una ruta relativa para que funcione sin problemas en cualquier sistema operativo (Windows/Linux). |
| `Files.writeString(...)` | Alta de alumnos | Escribe los datos directamente en el archivo de forma limpia. |
| `StandardOpenOption.CREATE` y `APPEND` | Parámetros de escritura | Garantiza que el fichero se cree si no existe y que los datos nuevos se añadan al final del documento. |
| `Files.lines(...)` | Lectura general y conteo | Lee el archivo línea a línea mediante un `Stream`, evitando consumir toda la memoria RAM. |
| `Files.readAllLines(...)` | Búsqueda por nombre | Carga el archivo en una lista indexada, lo que permite indicar al usuario en qué número de línea está el alumno buscado. |
| `stream.count()` | Conteo de registros | Cuenta los elementos directamente en el flujo de datos (streaming) de manera óptima. |

## 💡 Notas de Desarrollo y Buenas Prácticas

* **Codificación segura (UTF-8):** Todos los procesos de lectura y escritura aplican `StandardCharsets.UTF_8`. Esto evita que los acentos, las tildes y letras como la 'ñ' se corrompan al guardar.
* **Evaluación Perezosa (Lazy):** Para las operaciones de listar y contar, he optado por flujos de datos (`Stream`) en lugar de cargarlo todo de golpe. Si el archivo `alumnos.txt` creciera masivamente, la aplicación seguiría funcionando rápido y sin saturar la memoria.

---
*📦 **Contenido del repositorio:** Además de este README, se incluyen las clases `.java`, el fichero de pruebas y el informe en PDF con el funcionamiento detallado para la entrega.*

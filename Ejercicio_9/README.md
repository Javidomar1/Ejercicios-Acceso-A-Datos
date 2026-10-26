# Gestor de Productos RA1

Aplicación de consola en Java para la gestión de productos mediante ficheros de texto (CSV, TSV, JSON) implementando las operaciones CRUD clásicas mediante el patrón "leer-modificar-reescribir" y manejo de excepciones robusto.

## Instrucciones de compilación y ejecución
1. Ubícate en la raíz del proyecto (donde se encuentran los `.java`).
2. Compila los archivos:
   `javac GestorProductosRA1.java Producto.java ProductoInvalidoException.java`
3. Ejecuta la clase principal:
   `java GestorProductosRA1`

## Formatos Soportados
*   **CSV (datos/productos.csv):** Valores separados por `;`. Ideal para evitar conflictos con los decimales en formatos europeos. 
    *   *Nota sobre colisiones:* Si un producto incluye el carácter `;` en su nombre, el método `split(";")` corromperá el registro. En entornos de producción sería necesario envolver el texto en comillas (`"nombre;complejo"`).
*   **TSV (datos/productos.tsv):** Valores separados por tabulador (`\t`).
    *   *Nota sobre colisiones:* Al igual que en CSV, un tabulador introducido en el nombre romperá la estructura del registro.
*   **JSON (datos/productos.json):** Exportación estructural jerárquica con las claves `id`, `nombre`, `precio`.

## Decisiones tomadas
*   **Búsqueda silenciosa:** El método `buscarProductoPorId` retorna el objeto o `null` sin imprimir en consola, permitiendo su reutilización transparente en las validaciones de las altas, modificaciones y bajas.
*   **Gestión de Ficheros de Texto:** Dado que los CSV tienen longitud de registro variable, se implementa el patrón de volcado total en memoria (`leerTodasLasLineas`) y reescritura (`FileWriter(archivo, false)`) para las modificaciones y bajas[cite: 45].
*   **Manejo de Excepciones:** Se atrapan las `NumberFormatException` independientemente para evitar interrupciones al ingresar texto donde se espera un número, y la lógica se encapsula en bucles ininterrumpidos.

## Tabla de Pruebas Realizadas

| Caso | Entrada | Resultado Esperado | Resultado Obtenido |
| :--- | :--- | :--- | :--- |
| Listar sin productos | Fichero vacío / solo cabecera | Mensaje "No hay productos registrados." | Éxito |
| Añadir correcto | ID: 1, Nombre: Ratón, Precio: 15.5 | Producto guardado en CSV | Éxito |
| Añadir duplicado | ID: 1, Nombre: X, Precio: 10 | Error "El ID 1 ya existe." detectado | Éxito |
| Añadir con error formato | ID: "A" | Mensaje "Debes introducir un número válido." | Éxito |
| Buscar existente | ID: 1 | Muestra detalles del producto 1 | Éxito |
| Modificar | ID: 1, Nuevo Nombre: Teclado, Precio: 29.99 | Fichero reescrito con nuevos valores | Éxito |
| Eliminar | ID: 1 | Línea del ID 1 eliminada del CSV | Éxito |
| Convertir formatos | Seleccionar opción 6 | Se generan `.tsv` y `.json` válidos | Éxito |
| Copia seguridad | Seleccionar opción 8 | Archivo copiado a carpeta `backups/` | Éxito |

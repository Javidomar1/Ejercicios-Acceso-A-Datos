# GestorProductosRA1 - Gestor de Productos CSV / TSV / JSON

Aplicación Java de consola desarrollada para la gestión integral de productos almacenados en archivos planos (`CSV`, `TSV` y `JSON`). Ofrece operaciones CRUD completas, conversión dinámica de formatos, gestión automática de copias de seguridad con marcas de tiempo y persistencia atómica.

## 📋 Requisitos del Sistema

* **Java Development Kit (JDK):** Versión 17 o superior (compatible desde JDK 8).
* **Gestor de Construcción (opcional):** Apache Maven 3.6+.
* **Sistema Operativo:** Independiente (Windows, Linux, macOS).


## 📁 Estructura del Proyecto

```
Ejercicio_9/
├── datos/
│   ├── productos.csv        # Fichero principal de trabajo
│   ├── productos.tsv        # Exportación / Importación en TSV
│   └── productos.json       # Exportación / Importación en JSON (Extra)
├── backups/
│   └── productos_YYYYMMDD_HHMMSS.csv # Copias de seguridad automáticas
├── src/main/java/edu/ad/ficheros/ejercicio_9/
│   ├── GestorProductosRA1.java       # Clase principal con menú y lógica E/S
│   ├── Producto.java                 # Modelo de datos con validaciones
│   └── ProductoInvalidoException.java # Excepción personalizada
├── README.md                # Documentación del proyecto
└── pom.xml                  # Configuración del proyecto Maven
```

## 📄 Descripción de Formatos y Caracteres Especiales

### Formato CSV (`datos/productos.csv`)

* **Separador:** Punto y coma (`;`).
* **Estructura:** Primera línea reservada para la cabecera (`ID;NOMBRE;PRECIO`), seguida de un registro por línea.
* **Ejemplo:**

  ```csv
  ID;NOMBRE;PRECIO
  1;Teclado;29.99
  2;Ratón;15.50
  ```

### Formato TSV (`datos/productos.tsv`)

* **Separador:** Tabulador (`\t`).
* **Estructura:** Mantiene los mismos campos y orden que el CSV, utilizando tabulador como delimitador de campo.

### Formato JSON (`datos/productos.json` - Extra)

* **Estructura:** Arreglo de objetos JSON simples:

  ```json
  [
    {"id":1,"nombre":"Teclado","precio":29.99},
    {"id":2,"nombre":"Ratón","precio":15.50}
  ]
  ```

### ⚠️ Limitaciones y Manejo de Caracteres Especiales

Si el atributo `nombre` contiene el carácter del delimitador (por ejemplo, un punto y coma `;` en CSV o un tabulador `\t` en TSV):

* En la conversión simple mediante reemplazo directo (`replace(";", "\t")`), la presencia del separador dentro del texto del nombre modificaría el número de columnas resultantes al reinterpretar la línea.
* Para mitigar corrupciones, se aplica `split(";", -1)` conservando posiciones vacías y validando que el número exacto de columnas esperadas sea igual a 3. Se recomienda evitar el uso explícito de `;` o `\t` dentro de la cadena de texto del campo `nombre`.

## 🛠️ Decisiones de Diseño e Implementación

1. **Preparación Automática del Entorno:**
   Al arrancar la aplicación, se verifica y crea automáticamente la estructura de carpetas (`datos/` y `backups/`) con `Files.createDirectories` si no existen, así como la cabecera por defecto del fichero `productos.csv`.

2. **Validación Centralizada en el Modelo (`Producto`):**
   La integridad de los datos está encapsulada en el constructor de la clase `Producto`. Si se intenta instanciar un producto con `id <= 0`, `nombre` nulo/vacío o `precio < 0`, se lanza inmediatamente una excepción de tipo `ProductoInvalidoException`.

3. **Escritura Atómica mediante Archivo Temporal (`.tmp`):**
   En las operaciones de modificación y eliminación, en lugar de sobrescribir directamente el archivo de producción, el nuevo contenido se escribe en `datos/productos.csv.tmp`. Tras completarse la escritura correctamente, se reemplaza el archivo original mediante `Files.move` con la opción `StandardCopyOption.REPLACE_EXISTING`. Esto garantiza la protección contra pérdida de datos ante interrupciones imprevistas.

4. **Modo Append para Altas:**
   La incorporación de nuevos productos se realiza eficientemente mediante `FileWriter(file, true)`, evitando recargar la totalidad del fichero en memoria.

5. **Excepciones y Captura Robusta:**
   Toda lectura y escritura utiliza sintaxis `try-with-resources` asegurando el cierre de los buffers (`BufferedReader`, `BufferedWriter`). El bucle principal del `main` captura `NumberFormatException`, `IllegalArgumentException` e `IOException` evitando la terminación abrupta del programa.

## 🧪 Tabla de Pruebas Realizadas

| Nº | Caso de Prueba | Entrada de Prueba | Resultado Esperado | Resultado Obtenido | Estado |
| :---: | :--- | :--- | :--- | :--- | :---: |
| **1** | Inicialización de entorno | Archivo/carpetas no existen al iniciar | Creación de `datos/`, `backups/` y `productos.csv` con cabecera | Carpetas y archivo creados correctamente con cabecera `ID;NOMBRE;PRECIO` | **OK** |
| **2** | Listar con datos | Opción menú `1` | Mostrar listado formateado en consola | Muestra los productos procesados mediante `toString()` | **OK** |
| **3** | Añadir producto correcto | ID: `20`, Nombre: `Champu H&S`, Precio: `20.0` | Confirmación e inclusión al final del CSV | Producto añadido correctamente en modo *append* | **OK** |
| **4** | Añadir ID duplicado | ID: `20` (ya existente) | Mensaje de error sin modificar el fichero | `Error: El ID 20 ya existe en el fichero.` | **OK** |
| **5** | Buscar ID no existente | Opción menú `3`, ID: `1` | Retorno de `null` e informe de no encontrado | `No se encontró ningún producto con ID 1` | **OK** |
| **6** | Buscar ID existente | Opción menú `3`, ID: `2` | Retorno del objeto `Producto` y muestra en consola | `ENCONTRADO ID: 2 Nombre: Juanes \| Precio: 2000.0` | **OK** |
| **7** | Modificar producto existente | ID: `20`, Nuevo nombre: `Champu H&S`, Precio: `20.0` | Sustitución de la línea correspondiente | Fichero reescrito correctamente manteniendo la cabecera | **OK** |
| **8** | Eliminar producto existente | ID: `2` | Filtrado del ID y reemplazo atómico mediante `.tmp` | Producto eliminado y archivo actualizado sin corruptores | **OK** |
| **9** | Exportar CSV a TSV | Opción menú `6` | Creación de `datos/productos.tsv` con separador `\t` | Fichero `productos.tsv` generado conservando datos y cabecera | **OK** |
| **10** | Importar TSV a CSV | Opción menú `7` | Sobrescritura de CSV desde el TSV convirtiendo `\t` a `;` | Fichero `productos.csv` actualizado correctamente | **OK** |
| **11** | Crear copia de seguridad | Opción menú `8` | Generación de backup en `backups/productos_YYYYMMDD_HHMMSS.csv` | Copia creada con fecha y hora exacta (`productos_20261029_140253.csv`) | **OK** |
| **12** | Listar backups | Opción menú `9` | Muestra de todos los archivos guardados en `backups/` | Lista por pantalla los nombres de los ficheros de respaldo | **OK** |
| **13** | Exportar/Importar JSON (Extra) | Opciones menú `10` y `11` | Conversión bidireccional entre CSV y formato JSON | Fichero `productos.json` generado e importado sin pérdidas | **OK** |
| **14** | Validación de datos al añadir (Precio negativo) | ID: `30`, Nombre: `Jabón`, Precio: `-5.0` | Excepción `ProductoInvalidoException` capturada sin tocar el archivo | `Error de validación: El precio no puede ser negativo` | **OK** |
| **15** | Modificar/Eliminar ID inexistente | ID: `999` en opción Modificar o Eliminar | Verificación mediante `!existeID()`, cancelación previa y aviso | `No existe un producto con el ID 999` | **OK** |
| **16** | Entrada no numérica en consola | ID: `"abc"` o Precio: `"veinte"` | Captura de `NumberFormatException` sin cierre abrupto | `Error: formato de número inválido.` / `Debes introducir un número entero válido` | **OK** |

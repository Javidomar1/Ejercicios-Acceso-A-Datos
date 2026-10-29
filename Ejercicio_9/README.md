# Gestor de Productos

Aplicación de consola en Java para la gestión de un inventario de productos. Los datos se almacenan de forma persistente en archivos de texto, con soporte para operaciones CRUD (Crear, Leer, Actualizar, Eliminar), copias de seguridad y exportación/importación entre formatos.

## 🛠 Instrucciones de compilación y ejecución

El proyecto está diseñado para funcionar con **Java 8 o superior**. Los archivos pertenecen al paquete `edu.ad.ficheros.ejercicio_9`.

### Opción 1: Desde un IDE (NetBeans, Eclipse, IntelliJ)
1. Crea un nuevo proyecto Java en tu IDE preferido.
2. Copia los tres archivos (`GestorProductosRA1.java`, `Producto.java`, `ProductoInvalidoException.java`) respetando la estructura de paquetes: `src/edu/ad/ficheros/ejercicio_9/`.
3. Ejecuta el archivo principal `GestorProductosRA1.java`.

### Opción 2: Desde la terminal (Línea de comandos)
1. Abre una terminal y sitúate en el directorio raíz donde se encuentra la carpeta `edu`.
2. **Compilar los archivos:**
   ```bash
   javac edu/ad/ficheros/ejercicio_9/*.java
   ```
3. **Ejecutar la aplicación:**
   ```bash
   java edu.ad.ficheros.ejercicio_9.GestorProductosRA1
   ```
*(Nota: El programa creará automáticamente las carpetas `datos/` y `backups/` en el directorio desde donde se ejecute).*

---

## 📄 Descripción del formato CSV y TSV

El programa utiliza dos formatos de texto plano para el almacenamiento e intercambio de datos:

*   **CSV (Valores Separados por Comas - Adaptado):** 
    *   **Archivo:** `datos/productos.csv`
    *   **Separador:** Punto y coma (`;`). Se utiliza este carácter en lugar de la coma tradicional para evitar conflictos con los decimales de los precios.
    *   **Cabecera:** La primera línea siempre es `ID;NOMBRE;PRECIO`.
    *   **Ejemplo:** `1;Teclado Mecánico;45.50`

*   **TSV (Valores Separados por Tabuladores):**
    *   **Archivo:** `datos/productos.tsv`
    *   **Separador:** Tabulador (`\t`).
    *   **Cabecera:** La primera línea siempre es `ID\tNOMBRE\tPRECIO`.
    *   **Uso:** Se genera al elegir la opción "Exportar", facilitando la lectura directa en programas como Excel o Google Sheets.

---

## 🧠 Decisiones tomadas

Durante el desarrollo del sistema se han tomado las siguientes decisiones de diseño e implementación:

1.  **Uso de la API NIO.2 (`java.nio.file`):** Se priorizó el uso de `Paths` y `Files` para la creación de directorios y la manipulación atómica de archivos (ej. `Files.move` con `StandardCopyOption.REPLACE_EXISTING`), lo que hace el código más robusto y moderno frente al antiguo `java.io.File`.
2.  **Validación en el Constructor de `Producto`:** En lugar de validar los datos dispersos en el menú principal, las reglas de negocio (ID > 0, Nombre no vacío, Precio >= 0) se aplican directamente al instanciar el objeto. Si algo falla, lanza un `IllegalArgumentException`.
3.  **Prevención de Duplicados en Archivo:** Antes de instanciar un nuevo producto o modificarlo, el programa lee secuencialmente el CSV mediante el método `existeID()` para garantizar que la clave primaria (ID) no se repita, interrumpiendo el flujo si es necesario.
4.  **Actualización y Borrado (Patrón Temporal/Memoria):**
    *   *Modificación:* Lee todas las líneas en memoria (una `List<String>`), sustituye la línea del ID correspondiente y reescribe el archivo completo. Adecuado para archivos pequeños/medianos.
    *   *Eliminación:* Utiliza un archivo temporal `.tmp`. Se leen y escriben las líneas válidas, omitiendo la que se desea borrar. Finalmente, se reemplaza el archivo original de forma segura.
5.  **Formato de Backups:** Se decidió utilizar `LocalDateTime` y `DateTimeFormatter` para asegurar que cada copia de seguridad sea única y fácil de identificar por el usuario (formato: `productos_yyyyMMdd_HHmmss.csv`).

---

## 🧪 Tabla de pruebas realizadas

A continuación se detallan los casos de prueba manuales ejecutados para garantizar la estabilidad del programa:

| Caso de Prueba | Entrada de datos | Resultado Esperado | Resultado Obtenido (Éxito/Fallo) |
| :--- | :--- | :--- | :--- |
| **1. Añadir producto válido** | Opción 2 -> ID: `1`, Nombre: `Ratón`, Precio: `15.5` | Creación de objeto y guardado al final del CSV. | Éxito. Producto añadido correctamente. |
| **2. Evitar ID duplicado** | Opción 2 -> ID: `1` (ya existente) | El sistema aborta la creación y muestra un error. | Éxito. "Error: El ID 1 ya existe...". |
| **3. Validar precio negativo** | Opción 2 -> ID: `2`, Nombre: `Monitor`, Precio: `-10` | Se lanza excepción capturada en el bloque catch. | Éxito. "Error de validación: El precio no...".|
| **4. Buscar producto por ID** | Opción 3 -> ID: `1` | Lectura del CSV y muestra los detalles formateados. | Éxito. "ENCONTRADO -> ID: 1 \| Nombre...". |
| **5. Entrada de texto en ID** | Opción 3 -> ID: `abc` | Intercepción de `NumberFormatException`. | Éxito. "Error: Debes introducir un ID...". |
| **6. Modificar existente** | Opción 4 -> ID: `1`, Nombre: `Ratón Gaming`, Precio: `25.0` | El fichero CSV actualiza la línea correspondiente al ID 1. | Éxito. Datos actualizados al hacer listado. |
| **7. Eliminar producto** | Opción 5 -> ID: `1` | Creación de `.tmp`, omisión de la línea y sobreescritura. | Éxito. El producto ya no aparece en la lista. |
| **8. Crear Backup** | Opción 8 | Carpeta `backups/` contiene un archivo con fecha y hora. | Éxito. "Copia de seguridad creada...". |
| **9. Exportar a TSV** | Opción 6 | Fichero `productos.tsv` creado con las columnas tabuladas. | Éxito. Visualización correcta de tabuladores. |

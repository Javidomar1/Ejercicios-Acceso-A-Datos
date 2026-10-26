# 📂 Práctica de Manejo de Ficheros en Java

Practica en Java utilizando clases del paquete `java.io` (como `BufferedReader`, `BufferedWriter`, `FileReader` y `FileWriter`).

---

## 🚀 Ejercicios Desarrollados

### 🔹 Ejercicio 4: Contador de Palabras
* **Descripción:** Lee un fichero de texto (`file.txt`) y calcula el total de palabras que contiene..

### 🔹 Ejercicio 5: Buscador de Coincidencias (Estilo `grep`)
* **Descripción:** Solicita por consola una cadena de búsqueda y la ruta de un fichero. Muestra las líneas que contienen la coincidencia junto con su número de línea correspondiente.
* **Aspectos clave:** Lectura línea a línea con control de contador para replicar el comportamiento de herramientas CLI como `grep -n`.

### 🔹 Ejercicio 6: Registro Persistente de Alumnos
* **Descripción:** Permite ingresar nuevos alumnos y añadirlos a `students.txt` conservando siempre los registros previos.
* **Aspectos clave:** Escritura en modo anexión (*append mode*) instanciando `FileWriter(archivo, true)`.

### 🔹 Ejercicio 7: Copia Eficiente con Buffer
* **Descripción:** Clona el contenido íntegro de un archivo de origen en uno de destino.
* **Aspectos clave:** Optimización de I/O mediante el uso de `BufferedReader` y `BufferedWriter`, minimizando accesos a disco gracias al buffer intermedio.

---

## 📁 Estructura del Repositorio

```text
├── src/
│   ├── Ejercicio4.java
│   ├── Ejercicio5.java
│   ├── Ejercicio6.java
│   └── Ejercicio7.java
├── file.txt           # Archivo de prueba para lectura/conteo
├── students.txt       # Base de datos de texto para alumnos
└── README.md

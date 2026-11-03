# Gestor de Cuenta Corriente (Ficheros Binarios) 🏦

Aplicación de consola desarrollada en **Java** para gestionar los apuntes de una cuenta bancaria mediante la lectura y escritura directa en **archivos binarios de acceso aleatorio** (`RandomAccessFile`).

Este proyecto es una práctica académica enfocada en el control a bajo nivel de punteros, bytes y la persistencia de datos con registros de tamaño fijo.

---

## 🛠️ Conceptos Clave

* **Almacenamiento**: Fichero binario (`cuentas.dat`).
* **Acceso a Datos**: `java.io.RandomAccessFile` (modos `"r"` y `"rw"`).
* **Estructura de Datos**: Registros de tamaño estrictamente fijo (**60 bytes por registro**).
* **Tratamiento de Cadenas**: `StringBuffer` para forzar longitudes fijas de caracteres.

---

## 📐 Estructura del Registro (60 Bytes Fijos)

Para garantizar que el acceso directo (`seek()`) funcione matemáticamente, cada apunte se empaqueta en disco ocupando exactamente 60 bytes:

| Campo | Tipo Java | Operación I/O | Tamaño en Disco |
| :--- | :--- | :--- | :--- |
| **ID** | `int` | `writeInt()` | 4 bytes |
| **Fecha** | `LocalDate` | `writeLong(toEpochDay())` | 8 bytes |
| **Concepto** | `String` (20 chars) | `writeChars()` | 40 bytes (20 chars × 2) |
| **Importe** | `double` | `writeDouble()` | 8 bytes |
| | | **TOTAL** | **60 bytes** |

---

## 🚀 Funcionalidades (CRUD)

1. **Insertar (`Opción 1`)**: Alta masiva de una lista inicial de apuntes posicionando el puntero al final del fichero (`raf.seek(raf.length())`). Evita duplicados si el archivo ya tiene contenido.
2. **Listado Secuencial (`Opción 2`)**: Cálculo del total de registros e iteración mediante saltos exactos de 60 bytes para reconstruir e imprimir los objetos.
3. **Baja Física (`Opción 3`)**: Eliminación real de un registro. Localiza el ID, desplaza los registros inferiores un bloque (60 bytes) hacia arriba para cubrir el hueco y aplica `raf.setLength()` para recortar el sobrante final.
4. **Modificación Directa (`Opción 4`)**: Cambia el texto del "concepto" sobrescribiendo únicamente sus 40 bytes correspondientes. Salta estratégicamente el ID (4 bytes) y la fecha (8 bytes) dejando el resto del apunte intacto.
5. **Estadísticas (`Opción 5`)**: Cálculo matemático en tiempo real del peso del fichero y la cantidad de registros almacenados.

---

## 💻 Instrucciones de Ejecución

1. **Clona el repositorio:**
   ```bash
   git clone [https://github.com/tu-usuario/Ejer10GestorDeCuentaCorriente.git](https://github.com/tu-usuario/Ejer10GestorDeCuentaCorriente.git)
   cd Ejer10GestorDeCuentaCorriente

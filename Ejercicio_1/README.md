📁 Ejercicio 1: Crear carpetas y ficheros con Java NIO

En este primer ejercicio he hecho un programa de consola que monta una pequeña estructura de proyecto (carpetas y ficheros vacíos) y después la recorre para enseñar lo que hay dentro. Todo con java.nio.file.

📋 Enunciado

Crear la siguiente estructura y comprobar que existe:

Carpetas: src, bin, img y src/junit.
Ficheros: readme.md, src/run.java e img/imagen.png.
Recorrer el contenido del directorio (y subdirectorios) y mostrarlo por pantalla.

Entrega: el .java trabajado, que también tiene que estar en mi repositorio.

🎯 Qué hace el programa:

Crea las carpetas src, bin, img y src/junit. 
Antes de crear cada una comprueba si ya existe; si existe lo dice y no hace nada, y si no, la crea.

Crea los ficheros readme.md, src/run.java e img/imagen.png, con la misma comprobación previa (si ya están, no los vuelve a crear ni da error).

Recorre las carpetas y muestra por pantalla todo lo que contienen, con Files.walk.
Comprueba el readme.md de la raíz y, si existe, lo muestra también (no está dentro de ninguna de las carpetas, por eso lo trato aparte).

Lo más cómodo para entenderlo es ejecutarlo dos veces: la primera sale "Se ha creado..." y la segunda "ya existía...".

```java
try (Stream<Path> stream = Files.walk(dir)) {            // recorre dir y todas sus subcarpetas
    stream.forEach(p -> System.out.println(" - " + p));  // por cada ruta encontrada, la imprime
}                                                         // el try cierra el Stream solo al terminar
```

🛠️ Qué he usado y por qué
| | Para qué lo uso |
| :--- | :--- |
| `Paths.get(...)` | Construir las rutas. Las dejo relativas para que funcionen igual en Windows y Linux. |
| `Files.exists(...)` | Mirar si la carpeta o el fichero ya está antes de crearlo. |
| `Files.createDirectories(...)` | Crear la carpeta y, si hace falta, las intermedias. Por eso `src/junit` funciona aunque `src` no exista todavía. |
| `Files.createFile(...)` | Crear los ficheros vacíos. |
| `Files.walk(...)` | Recorrer cada carpeta con sus subcarpetas, devolviendo un `Stream`. |

Dejé las rutas en dos arrays (carpetas y fichero) y las recorro con un for. Así, si quiero añadir otra carpeta o fichero, solo toco el array y no repito código.

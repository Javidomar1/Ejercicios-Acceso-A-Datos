/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package edu.ad.ficheros.ejercicio_9;

import edu.ad.ficheros.ejercicio_9.Producto;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Gestor de productos por consola sobre un fichero CSV.
 *
 * @author javier.dommar
 */
public class GestorProductosRA1 {

    /**
     * Ruta del fichero CSV de productos.
     */
    public static Path rutacsv = Paths.get("datos/productos.csv");
    /**
     * Scanner compartido para leer de consola.
     */
    public static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {

        Path rutadatos = Paths.get("datos/");
        Path rutabackups = Paths.get("backups/");
        try {
            if (!Files.exists(rutadatos)) {
                Files.createDirectories(rutadatos);
            }

            if (!Files.exists(rutabackups)) {
                Files.createDirectories(rutabackups);
            }
            if (!Files.exists(rutacsv)) {
                Files.createFile(rutacsv);
                try (BufferedWriter bw = new BufferedWriter(new FileWriter(rutacsv.toFile()))) {
                    bw.write("ID;NOMBRE;PRECIO");
                    bw.newLine();
                }
            }

        } catch (IOException e) {
            System.out.println("Hubo un error: " + e);
        }

        while (true) {
            System.out.println("\n--- GESTOR DE PRODUCTOS ---");
            System.out.println("1. Listar productos");
            System.out.println("2. Añadir producto");
            System.out.println("3. Buscar producto por ID");
            System.out.println("4. Modificar producto");
            System.out.println("5. Eliminar producto");
            System.out.println("6. Exportar CSV a TSV");
            System.out.println("7. Importar TSV a CSV");
            System.out.println("8. Crear copia de seguridad");
            System.out.println("9. Listar copias de seguridad");
            System.out.println("10. Exportar CSV a JSON");
            System.out.println("11. Importar JSON a CSV");
            System.out.println("0. Salir");

            String opcion = sc.nextLine();

            switch (opcion) {
                case "1":
                    listarProductos();
                    break;
                case "2":
                    añadirProducto();
                    break;
                case "3":
                    System.out.print("Escribe el ID del producto a buscar: ");
                    try {
                        int idBuscar = Integer.parseInt(sc.nextLine());
                        Producto p = buscarProductoPorId(idBuscar);
                        if (p != null) {
                            System.out.println("ENCONTRADO -> " + p.toString());
                        } else {
                            System.out.println("No se encontró ningún producto con ID " + idBuscar);
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Error: Debes introducir un ID numérico.");
                    } catch (IOException e) {
                        System.out.println("Error de lectura: " + e.getMessage());
                    }
                    break;
                case "4":
                    modificarProducto();
                    break;
                case "5":
                    eliminarProducto();
                    break;
                case "6":
                    exportarATsv();
                    break;
                case "7":
                    importarDeTsv();
                    break;
                case "8":
                    crearCopiaSeguridad();
                    break;
                case "9":
                    listarBackups();
                    break;
                case "10":
                    exportarAJson();
                    break;
                case "11":
                    importarDeJson();
                    break;
                case "0":
                    System.out.println("¡Saliendo del programa. Hasta pronto!");
                    return;
                default:
                    System.out.println("Opción no válida.");
            }
        }

    }

    /**
     * Pide por consola ID, nombre y precio, y añade el producto al final del
     * fichero CSV si el ID no está repetido.
     */
    public static void añadirProducto() {
        try {
            System.out.println("Escribe el id del producto");
            int id = Integer.parseInt(sc.nextLine());
            //Antes de tocar el archivo o crear el objeto, comprobamos si el ID ya existe leyendo el CSV.
            //Si existe, cortamos la ejecución con 'return' para evitar duplicados.
            if (existeID(id)) {
                System.out.println("Error: El ID " + id + " ya existe en el fichero.");
                return;
            }
            System.out.println("Introduce el nombre:");
            String nombre = sc.nextLine();
            System.out.print("Introduce el precio: ");
            double precio = Double.parseDouble(sc.nextLine());
            //Crea el objeto en memoria.
            //Si el precio es negativo o el nombre está vacío,
            //el constructor lanzará un IllegalArgumentException que será atrapado más abajo.
            Producto p = new Producto(id, nombre, precio);
            //FileWriter(ruta, true): el segundo parámetro 'true' activa el modo append
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(rutacsv.toFile(), true))) {
                //los datos del objeto los separamos con punto y coma, formando la línea estructurada.
                bw.write(p.getID() + ";" + p.getNombre() + ";" + p.getPrecio());
                bw.newLine();
                System.out.println("Producto añadido correctamente.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Error: Formato de número inválido.");
        } catch (IllegalArgumentException e) {
            System.out.println("Error de validación: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Error de escritura: " + e.getMessage());
        }

    }

    /**
     * Lee el archivo CSV secuencialmente, salta la cabecera y muestra por
     * consola todos los productos registrados.
     */
    public static void listarProductos() {

        try (BufferedReader br = Files.newBufferedReader(rutacsv)) {
            br.readLine();
            String linea;
            //
            while ((linea = br.readLine()) != null) {
                //Corta la línea de texto cada vez que encuentra un punto y coma (;)
                String[] campos = linea.split(";", -1);
                //Comprueba que el split haya generado exactamente 3 trozos (ID, Nombre, Precio)
                //y que el primer trozo (el ID) no esté vacío
                // -1 para no perder campos vacíos
                if (campos.length == 3 && !campos[0].isEmpty()) {
                    int id = Integer.parseInt(campos[0]);
                    String nombre = campos[1];
                    double precio = Double.parseDouble(campos[2]);

                    // Creamos el objeto e imprimimos usando su toString()
                    Producto producto = new Producto(id, nombre, precio);
                    System.out.println(producto.toString());
                }
            }

        } catch (FileNotFoundException e) {
            System.err.println("Fichero no encontrado: " + e.getMessage()); 
        } catch (IOException e) {
            System.err.println("Error de lectura: " + e.getMessage());
        }
    }

    /**
     * Comprueba si ya existe un producto con el ID indicado en el CSV.
     *
     * @param idBuscar ID a comprobar
     * @return {@code true} si el ID existe, {@code false} en caso contrario
     * @throws IOException si ocurre un error al leer el fichero
     */
    public static boolean existeID(int idBuscar) throws IOException {
        File archivo = rutacsv.toFile();
        //Si no existe el fichero, el ID no puede estar
        if (!archivo.exists()) {
            return false;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            boolean primeraLinea = true;
            //Saltar cabecera
            while ((linea = br.readLine()) != null) {
                if (primeraLinea) {
                    primeraLinea = false;
                    continue;
                }
                // -1 para no perder campos vacíos
                String[] campos = linea.split(";", -1);
                if (campos.length >= 1 && !campos[0].isEmpty()) {
                    int idActual = Integer.parseInt(campos[0]);
                    if (idActual == idBuscar) {
                        return true;
                    }
                }
            }
            return false;
        }
    }

    /**
     * Busca un producto por su ID en el fichero CSV, sin imprimir nada.
     *
     * @param idBuscar ID del producto a buscar
     * @return el {@link Producto} encontrado, o {@code null} si no existe
     * @throws IOException si ocurre un error al leer el fichero
     */
    public static Producto buscarProductoPorId(int idBuscar) throws IOException {
        File archivo = rutacsv.toFile();
        if (!archivo.exists()) {
            return null;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            boolean primeraLinea = true;

            while ((linea = br.readLine()) != null) {
                if (primeraLinea) {
                    primeraLinea = false;
                    continue;
                }
                String[] campos = linea.split(";", -1);
                //Valida que la línea tenga exactamente 3 campos
                if (campos.length == 3 && !campos[0].isEmpty()) {
                    int idActual = Integer.parseInt(campos[0]);
                    //Compara el ID actual del archivo con el que estamos buscando.
                    if (idActual == idBuscar) {
                        String nombre = campos[1];
                        double precio = Double.parseDouble(campos[2]);
                        //Al encontrarlo, construye y devuelve el objeto Producto
                        return new Producto(idActual, nombre, precio);
                    }
                }
            }
        }
        return null;
    }

    /**
     * Solicita un ID al usuario y, si existe, pide los nuevos datos para
     * actualizarlo. Utiliza el patrón de lectura total en memoria y reescritura
     * completa del archivo.
     *
     */
    public static void modificarProducto() {
        try {
            System.out.print("ID del producto a modificar: ");
            int id = Integer.parseInt(sc.nextLine());

            if (!existeID(id)) {
                System.out.println("No existe un producto con ID " + id);
                return;
            }

            System.out.print("Nuevo nombre: ");
            String nombre = sc.nextLine();
            System.out.print("Nuevo precio: ");
            double precio = Double.parseDouble(sc.nextLine());

            //Se crea antes de tocar el fichero: si los datos no son válidos, salta la excepción aquí
            Producto p = new Producto(id, nombre, precio);

            //Leer todas las líneas
            List<String> lineas = new ArrayList<>();
            try (BufferedReader br = new BufferedReader(new FileReader(rutacsv.toFile()))) {
                String linea;
                while ((linea = br.readLine()) != null) {
                    lineas.add(linea);
                }
            }

            //Localizar el ID y sustituir la línea (i = 1 salta la cabecera)
            for (int i = 1; i < lineas.size(); i++) {
                String[] campos = lineas.get(i).split(";", -1);
                if (campos[0].equals(String.valueOf(id))) {
                    lineas.set(i, p.getID() + ";" + p.getNombre() + ";" + p.getPrecio());
                    break;
                }
            }

            //Reescribir el fichero completo (sin append)
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(rutacsv.toFile()))) {
                for (String l : lineas) {
                    bw.write(l);
                    bw.newLine();
                }
            }
            System.out.println("Producto modificado correctamente.");

        } catch (NumberFormatException e) {
            System.out.println("Error: formato de número inválido.");
        } catch (IllegalArgumentException e) {
            System.out.println("Error de validación: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Error de lectura/escritura: " + e.getMessage());
        }
    }

    /**
     * Pide por consola un ID y elimina el producto correspondiente del fichero
     * CSV, reescribiéndolo completo sin esa línea.
     */
    public static void eliminarProducto() {
        try {
            System.out.print("Escribe el ID del producto a eliminar: ");
            int idBuscar = Integer.parseInt(sc.nextLine());

            //Verificamos que el ID exista antes de tocar ficheros
            if (!existeID(idBuscar)) {
                System.out.println("No existe ningún producto con el ID " + idBuscar);
                return;
            }

            //Leer todas las líneas conservando las que NO coinciden con el ID
            List<String> lineasConservadas = new ArrayList<>();
            try (BufferedReader br = new BufferedReader(new FileReader(rutacsv.toFile()))) {
                String linea = br.readLine();
                if (linea != null) {
                    lineasConservadas.add(linea); // Conservamos la cabecera (ID;NOMBRE;PRECIO)
                }

                while ((linea = br.readLine()) != null) {
                    String[] campos = linea.split(";", -1);
                    if (campos.length >= 1 && !campos[0].isEmpty()) {
                        int idActual = Integer.parseInt(campos[0]);
                        if (idActual != idBuscar) {
                            lineasConservadas.add(linea); // Solo guardamos los que NO se van a eliminar
                        }
                    }
                }
            }

            //Reescribir a un fichero temporal (.tmp) por seguridad
            Path rutaTemp = Paths.get("datos/productos.csv.tmp");
            try (BufferedWriter bw = Files.newBufferedWriter(rutaTemp)) {
                for (String l : lineasConservadas) {
                    bw.write(l);
                    bw.newLine();
                }
            }

            // Reemplazo el original por el temporal
            Files.move(rutaTemp, rutacsv, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("Producto eliminado correctamente.");

        } catch (NumberFormatException e) {
            System.out.println("Error: Debes introducir un número entero válido para el ID.");
        } catch (IOException e) {
            System.out.println("Error de E/S al intentar eliminar: " + e.getMessage());
        }
    }

    /**
     * Lee los datos del archivo CSV principal y genera copias exportadas en
     * formato TSV (separado por tabuladores).
     */
    public static void exportarATsv() {
        Path rutatsv = Paths.get("datos/productos.tsv");

        try (BufferedReader br = new BufferedReader(new FileReader(rutacsv.toFile())); BufferedWriter bw = new BufferedWriter(new FileWriter(rutatsv.toFile()))) {

            String linea;
            while ((linea = br.readLine()) != null) {
                // Se reemplazan los punto y coma por tabuladores para crear el TSV
                String lineaTsv = linea.replace(";", "\t");
                bw.write(lineaTsv);
                bw.newLine();
            }
            System.out.println("Fichero exportado de CSV a TSV correctamente.");
            /*
             * ¿Qué ocurre si un nombre contiene ';' o un tabulador?
             * Si usamos un replace() o split() simple, el delimitador extra dividirá 
             * el campo en dos. Por ejemplo, "Teclado; mecánico" pasaría a tener 4 columnas 
             * en lugar de 3. Esto corrompe el array resultante y causará una 
             * ArrayIndexOutOfBoundsException o NumberFormatException al leerlo. 
             * Para evitarlo deben ir entrecomillas.
             */
        } catch (IOException e) {
            System.out.println("Error al exportar a TSV: " + e.getMessage());
        }
    }

    /**
     * Lee el archivo TSV y sobrescribe el contenido del archivo CSV principal,
     * transformando los tabuladores nuevamente en separadores de punto y coma
     * (;).
     */
    public static void importarDeTsv() {
        Path rutatsv = Paths.get("datos/productos.tsv");
        if (!Files.exists(rutatsv)) {
            System.out.println("Error: El archivo productos.tsv no existe.");
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(rutatsv.toFile())); BufferedWriter bw = new BufferedWriter(new FileWriter(rutacsv.toFile()))) {

            String linea;
            while ((linea = br.readLine()) != null) {
                // Se reemplazan los tabuladores por punto y coma para volver a CSV
                String lineaCsv = linea.replace("\t", ";");
                bw.write(lineaCsv);
                bw.newLine();
            }
            System.out.println("Fichero importado desde TSV a CSV correctamente.");

        } catch (IOException e) {
            System.out.println("Error al importar desde TSV: " + e.getMessage());
        }
    }

    /**
     * Realiza una copia de seguridad exacta del archivo CSV en la carpeta
     * backups, añadiendo una marca de tiempo al nombre del archivo.
     */
    public static void crearCopiaSeguridad() {
        try {
            // Obtiene la fecha/hora exacta actual (LocalDateTime) y 
            //crea una regla de formato (DateTimeFormatter) para convertirla en texto "AñoMesDía_HoraMinSeg".
            String fecha = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            Path origen = rutacsv;
            Path destino = Paths.get("backups/productos_" + fecha + ".csv");

            if (Files.exists(origen)) {
                Files.copy(origen, destino, StandardCopyOption.REPLACE_EXISTING);
                System.out.println("Copia de seguridad creada: " + destino.getFileName());
            } else {
                System.out.println("No hay datos para respaldar.");
            }
        } catch (IOException e) {
            System.out.println("Error al crear copia: " + e.getMessage());
        }
    }

    /**
     * Escanea el directorio de copias de seguridad y muestra por consola los
     * nombres de todos los archivos de respaldo disponibles.
     */
    public static void listarBackups() {
        File dir = new File("backups/");
        File[] archivos = dir.listFiles();
        if (archivos == null || archivos.length == 0) {
            System.out.println("No hay copias de seguridad disponibles.");
        } else {
            System.out.println("--- COPIAS DE SEGURIDAD ---");
            for (File f : archivos) {
                System.out.println("- " + f.getName());
            }
        }
    }

    /**
     * Exporta el contenido del archivo CSV original a un archivo en formato
     * JSON simple.
     * <p>
     * Este método lee el fichero {@code productos.csv}, omite la cabecera y
     * transforma cada registro en un objeto JSON dentro de un array. La
     * escritura se realiza con un bloque try-with-resources para garantizar el
     * cierre seguro de los flujos.
     * </p>
     */
    public static void exportarAJson() {
        Path rutaJson = Paths.get("datos/productos.json");

        try (BufferedReader br = Files.newBufferedReader(rutacsv); BufferedWriter bw = Files.newBufferedWriter(rutaJson)) {

            br.readLine(); // Saltamos la cabecera
            String linea;
            boolean primero = true;

            bw.write("["); // Abrimos el array JSON
            bw.newLine();

            while ((linea = br.readLine()) != null) {
                String[] campos = linea.split(";", -1);
                if (campos.length == 3 && !campos[0].isEmpty()) {
                    if (!primero) {
                        bw.write(",");
                        bw.newLine();
                    }
                    // Construimos la línea con formato JSON 
                    // String.format rellena la plantilla: cada %s se sustituye por un valor (campos[0], [1], [2]) en orden.
                    // Las \" son comillas para que salgan dentro del texto del JSON y no cierren el String de Java
                    String jsonObjeto = String.format("  {\"id\":%s,\"nombre\":\"%s\",\"precio\":%s}",
                            campos[0], campos[1], campos[2]);
                    bw.write(jsonObjeto);
                    primero = false;
                }
            }

            bw.newLine();
            bw.write("]"); // Cerramos el array JSON
            System.out.println("Fichero exportado a JSON correctamente.");

        } catch (IOException e) {
            System.out.println("Error al exportar a JSON: " + e.getMessage());
        }
    }

    /**
     * Importa los datos de un archivo JSON simple y sobrescribe el fichero CSV
     * principal.
     * <p>
     * Este método lee el fichero {@code productos.json} secuencialmente,
     * extrayendo los valores de ID, nombre y precio mediante manipulación de
     * cadenas (sin librerías externas), y los reescribe en
     * {@code productos.csv} restaurando la cabecera y los separadores ( ; ).
     * </p>
     */
    public static void importarDeJson() {
        Path rutaJson = Paths.get("datos/productos.json");

        if (!Files.exists(rutaJson)) {
            System.out.println("Error: El archivo JSON no existe.");
            return;
        }

        try (BufferedReader br = Files.newBufferedReader(rutaJson); BufferedWriter bw = Files.newBufferedWriter(rutacsv)) {
            // Devolvemos la cabecera obligatoria
            bw.write("ID;NOMBRE;PRECIO"); 
            bw.newLine();

            String linea;
            while ((linea = br.readLine()) != null) {
                linea = linea.trim();

                // Ignorar apertura/cierre de array y líneas vacías
                if (linea.equals("[") || linea.equals("]") || linea.isEmpty()) {
                    continue;
                }

                try {
                    // Extraemos los valores ubicando las etiquetas JSON
                    String id = linea.substring(linea.indexOf("\"id\":") + 5, linea.indexOf(",\"nombre\"")).trim();
                    String nombre = linea.substring(linea.indexOf("\"nombre\":\"") + 10, linea.indexOf("\",\"precio\""));
                    String precio = linea.substring(linea.indexOf("\"precio\":") + 9, linea.indexOf("}")).trim();

                    bw.write(id + ";" + nombre + ";" + precio);
                    bw.newLine();
                } catch (Exception ex) {
                    System.out.println("Línea JSON ignorada por formato inválido: " + linea);
                }
            }
            System.out.println("Fichero importado desde JSON correctamente.");

        } catch (IOException e) {
            System.out.println("Error al importar desde JSON: " + e.getMessage());
        }
    }
}

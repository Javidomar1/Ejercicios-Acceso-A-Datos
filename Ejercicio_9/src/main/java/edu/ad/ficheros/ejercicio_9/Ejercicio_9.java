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

/**
 *
 * @author javier.dommar
 */
public class Ejercicio_9 {

    public static Path rutacsv = Paths.get("datos/productos.csv");
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

            String opcion = sc.nextLine();

            switch (opcion) {
                case "1":
                    listarProductos();
                    break;
                case "2":
                    añadirProducto();
                    break;
                case "3":
                    System.out.print("Escríbeme el ID del producto a buscar: ");
                    try {
                        int idBuscar = Integer.parseInt(sc.nextLine());
                        Producto p = buscarProductoPorId(idBuscar); // Busca sin imprimir
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
                default:
                    System.out.println("Opción no válida.");
            }
        }

    }

    public static void añadirProducto() {
        try {
            System.out.println("Escribe el id del producto");
            int id = Integer.parseInt(sc.nextLine());
            if (existeID(id)) {
                System.out.println("Error: El ID " + id + " ya existe en el fichero.");
                return;
            }
            System.out.println("Introduce el nombre:");
            String nombre = sc.nextLine();
            System.out.print("Introduce el precio: ");
            double precio = Double.parseDouble(sc.nextLine());

            Producto p = new Producto(id, nombre, precio);
            //FileWriter(ruta, true): el segundo parámetro 'true' activa el modo append
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(rutacsv.toFile(), true))) {
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

    public static void listarProductos() {
        try (BufferedReader br = Files.newBufferedReader(rutacsv)) {
            br.readLine();
            String linea;

            while ((linea = br.readLine()) != null) {
                String[] campos = linea.split(";", -1); // Conserva tokens vacíos

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
            System.err.println("Fichero no encontrado: " + e.getMessage()); // Captura específica primero
        } catch (IOException e) {
            System.err.println("Error de lectura: " + e.getMessage());
        }
    }

    public static boolean existeID(int idBuscar) throws IOException {
        File archivo = rutacsv.toFile();
        if (!archivo.exists()) {
            return false;
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

                if (campos.length == 3 && !campos[0].isEmpty()) {
                    int idActual = Integer.parseInt(campos[0]);

                    if (idActual == idBuscar) {
                        String nombre = campos[1];
                        double precio = Double.parseDouble(campos[2]);
                        return new Producto(idActual, nombre, precio); // Lo devuelve limpio, sin imprimir
                    }
                }
            }
        }
        return null;
    }

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

            // Se crea antes de tocar el fichero: si los datos no son válidos, salta la excepción aquí
            Producto p = new Producto(id, nombre, precio);

            // 1. Leer todas las líneas
            List<String> lineas = new ArrayList<>();
            try (BufferedReader br = new BufferedReader(new FileReader(rutacsv.toFile()))) {
                String linea;
                while ((linea = br.readLine()) != null) {
                    lineas.add(linea);
                }
            }

            // 2. Localizar el ID y sustituir la línea (i = 1 salta la cabecera)
            for (int i = 1; i < lineas.size(); i++) {
                String[] campos = lineas.get(i).split(";", -1);
                if (campos[0].equals(String.valueOf(id))) {
                    lineas.set(i, p.getID() + ";" + p.getNombre() + ";" + p.getPrecio());
                    break;
                }
            }

            // 3. Reescribir el fichero completo (sin append)
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

    public static void eliminarProducto() {
        try {
            System.out.print("Escribe el ID del producto a eliminar: ");
            int idBuscar = Integer.parseInt(sc.nextLine());

            // 1. Verificamos que el ID exista antes de tocar ficheros
            if (!existeID(idBuscar)) {
                System.out.println("No existe ningún producto con el ID " + idBuscar);
                return;
            }

            // 2. Leer todas las líneas conservando las que NO coinciden con el ID
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

            // 3. Reescribir a un fichero temporal (.tmp) por seguridad
            Path rutaTemp = Paths.get("datos/productos.csv.tmp");
            try (BufferedWriter bw = Files.newBufferedWriter(rutaTemp)) {
                for (String l : lineasConservadas) {
                    bw.write(l);
                    bw.newLine();
                }
            }

            // 4. Reemplazar el fichero original de forma atómica con NIO.2
            Files.move(rutaTemp, rutacsv, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("Producto eliminado correctamente.");

        } catch (NumberFormatException e) {
            System.out.println("Error: Debes introducir un número entero válido para el ID.");
        } catch (IOException e) {
            System.out.println("Error de E/S al intentar eliminar: " + e.getMessage());
        }
    }

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
            /*
             * ¿Qué ocurre si un nombre contiene ';' o un tabulador?
             * Si usamos un replace() o split() simple, el delimitador extra dividirá 
             * el campo en dos. Por ejemplo, "Teclado; mecánico" pasaría a tener 4 columnas 
             * en lugar de 3. Esto corrompe el array resultante y causará una 
             * ArrayIndexOutOfBoundsException o NumberFormatException al leerlo. 
             * Para evitarlo en entornos reales, los campos conflictivos deben ir entrecomillados.
             */
        } catch (IOException e) {
            System.out.println("Error al exportar a TSV: " + e.getMessage());
        }
    }

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
}

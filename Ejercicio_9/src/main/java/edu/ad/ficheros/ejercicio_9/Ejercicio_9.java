/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package edu.ad.ficheros.ejercicio_9;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

/**
 *
 * @author javier.dommar
 */
public class Ejercicio_9 {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Path rutadatos = Paths.get("datos/");
        Path rutacsv = Paths.get("datos/productos.csv");
        Path rutabackups = Paths.get("backups/");
        try {
            if (!Files.exists(rutadatos)) {
                Files.createDirectories(rutadatos);
            }

            if (!Files.exists(rutabackups)) {
                Files.createDirectories(rutabackups);
            }
            if (!Files.exists(rutacsv)) {
                Files.createDirectories(rutacsv);
            }
            if (!Files.exists(rutacsv)) {
                try (BufferedWriter bw = new BufferedWriter(new FileWriter(rutacsv.toFile()))) {
                    bw.write("ID;NOMBRE;PRECIO");
                    bw.newLine();
                }
            }
        } catch (IOException e) {
            System.out.println("Hubo un error: " + e);
        }
        try (BufferedReader br = new BufferedReader(new FileReader(rutacsv.toFile()))) {
            String linea;
            String PrimeraLinea = "ID;NOMBRE;PRECIO";
            Boolean encontrada = false;
            while (((linea = br.readLine()) != null)) {
                if (linea == PrimeraLinea) {
                    encontrada = true;
                    continue;
                }
                String[] campos = linea.split(";", -1);
                int id = Integer.parseInt(campos[0]) ;
                String nombre = campos[1];
                double precio = Double.parseDouble(campos[2]);
                System.out.println(" El id: "+ id + " El nombre " + nombre + " El precio " + precio);
            }
        } catch (IOException e) {
            System.out.println("Hubo un error en la lectura" + e);
        }
    }
}

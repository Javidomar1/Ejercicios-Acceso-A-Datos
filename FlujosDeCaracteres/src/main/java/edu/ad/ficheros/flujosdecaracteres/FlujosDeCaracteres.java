/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package edu.ad.ficheros.flujosdecaracteres;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 *
 * @author javier.dommar
 */
public class FlujosDeCaracteres {

    public static void main(String[] args) {
        File archivo = new File("file.txt");
        int totalPalabras = 0;
        if (!archivo.exists()) {
            System.out.println("El fichero no existe.");
            return;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                linea = linea.trim();
                if (!linea.isEmpty()) {
                    String[] palabras = linea.split("\\s+");
                    totalPalabras += palabras.length;
                }
            }
        } catch (IOException e) {
            System.err.println("Error durante la copia: " + e.getMessage());
        }
        System.out.println("El fichero contiene un total de: " + totalPalabras + " palabras.");

        //*Descubri otra manera de leerlo es con Files.readAllLines *//
        totalPalabras = 0;
        Path archivo2 = Paths.get("file.txt");
        try {
            List<String> lineas = Files.readAllLines(archivo2, StandardCharsets.UTF_8);
            for (String linea : lineas) {
                if (!linea.isEmpty()) {
                    String[] palabras = linea.split("\\s+");
                    totalPalabras += palabras.length;
                }
            }
        } catch (IOException e) {
            System.err.println("Error al leer el fichero: " + e.getMessage());
        }
        System.out.println("El fichero contiene un total de: " + totalPalabras + " palabras.");
    }
}

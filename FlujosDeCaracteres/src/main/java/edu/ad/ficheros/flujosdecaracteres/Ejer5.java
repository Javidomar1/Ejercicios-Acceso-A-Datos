/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.ad.ficheros.flujosdecaracteres;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;


/**
 *
 * @author javier.dommar
 */
public class Ejer5 {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Escribeme el fichero a leer");
        String Fichero = sc.nextLine();
        File archivo = new File(Fichero);
        System.out.println("Escribme la palabra a buscar");
        String Palabra = sc.nextLine();
        int totalPalabras = 0;
        if (!archivo.exists()) {
            System.out.println("El fichero no existe.");
            return;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                linea = linea.trim();
                if (linea.contains(Palabra) && !linea.isEmpty()) {
                    String[] palabras = linea.split("\\s+");
                    totalPalabras += palabras.length;
                }
            }
        } catch (IOException e) {
            System.err.println("Error durante la copia: " + e.getMessage());
        }
        System.out.println("El fichero contiene un total de: " + totalPalabras + " palabras.");

    }
}

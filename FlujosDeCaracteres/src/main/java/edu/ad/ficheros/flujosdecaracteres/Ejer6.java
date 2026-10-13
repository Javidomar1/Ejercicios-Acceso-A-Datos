/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.ad.ficheros.flujosdecaracteres;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author javier.dommar
 */
public class Ejer6 {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Escribe que quieres añadir");
        String Texto = sc.nextLine();
        String []Palabras = Texto.split(";");
        Path archivo = Path.of("students.txt");
        try {
            Files.writeString(archivo, Palabras, StandardCharsets.UTF_8, StandardOpenOption.APPEND);
        } catch (IOException e) { //[cite: 41, 71]
            System.err.println("Error al escribir en el fichero: " + e.getMessage());
        }
        try(BufferedReader br = new BufferedReader (new FileReader(archivo.toString()))){
            String linea;
            while((linea = br.readLine()) != null){
                System.out.println(linea);
            }
        } catch (IOException ex) {
            System.out.println("El error fue: " + ex);
        }
    }
}

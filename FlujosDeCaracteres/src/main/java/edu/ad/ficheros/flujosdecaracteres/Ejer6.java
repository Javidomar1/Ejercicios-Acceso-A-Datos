/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.ad.ficheros.flujosdecaracteres;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Scanner;

/**
 *
 * @author javier.dommar
 */
public class Ejer6 {

    public static void main(String[] args) {
        //Declaramos el Scanner
        
        Scanner sc = new Scanner(System.in);
        
        //Le pedimos por pantalla que quiere el usuari añadir
        
        System.out.println("Escribe que quieres añadir");
        String Texto = sc.nextLine();
        Path archivo = Path.of("student.txt");
        if(!Files.exists(archivo)){
            // le pongo una comprobacion de que si no existeno no haga el try
            // dado que es mejor un mensaje explicativo que un Exception
            System.out.println("No existe el archivo ");
        }
        try {
            Files.writeString(archivo,Texto, StandardCharsets.UTF_8, StandardOpenOption.APPEND);
        } catch (IOException e) { 
            System.err.println("Error al escribir en el fichero: " + e.getMessage());
        }
        try(BufferedReader br = new BufferedReader (new FileReader(archivo.toString()))){
            String linea;
            while((linea = br.readLine()) != null){
                System.out.println(linea);
            }
        } catch (IOException e) {
            System.out.println("El error fue: " + e);
        }
    }
}

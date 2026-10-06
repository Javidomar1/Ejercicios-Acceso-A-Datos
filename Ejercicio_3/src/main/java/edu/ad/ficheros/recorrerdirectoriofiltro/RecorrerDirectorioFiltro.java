/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package edu.ad.ficheros.recorrerdirectoriofiltro;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

/**
 *
 * @author javier.dommar
 */
public class RecorrerDirectorioFiltro {

	public static void main(String[] args) {

		// --- FORMA 1: Clase anonima (estilo clasico) ---
		// Creamos el directorio y los ficheros .txt para luego
		// poder comprobarlos
		File directorio = new File("datos");
		File fichero = new File("datos/alumnos.txt");
		try {
			if (!directorio.exists()) {
				directorio.mkdir();
			}
			if (!fichero.exists()) {
				fichero.createNewFile();
				System.out.println("Archivo de prueba creado en disco.\n");
			}
		} catch (IOException e) {
			System.err.println("Error al crear el archivo: " + e.getMessage());
		}
		FilenameFilter soloTxt = new FilenameFilter() {
			@Override
			public boolean accept(File dir, String nombre) {
				return nombre.toLowerCase().endsWith(".txt");
			}
		};
		File[] FicherosTxt = directorio.listFiles(soloTxt);
		System.out.println("Ficheros .txt encontrados:");
		if (FicherosTxt != null) {
			for (File f : FicherosTxt) {
				System.out.println(" " + f.getName() + " (" + f.length() + "bytes" + ")");
			}
		}

		// --- FORMA 2: Expresion lambda (Java 8+, forma preferida) ---

		// Cambiamos la ruta para buscar los archivos .java del proyecto.

		directorio = new File("src\\main\\java\\edu\\ad\\ficheros\\recorrerdirectoriofiltro");
		File[] ficherosJava = directorio.listFiles((dir, nombre) -> nombre.endsWith(".java"));
		System.out.println("Ficheros .java encontrados:");
		if (ficherosJava != null) {
			for (File f : ficherosJava) {
				System.out.println(" " + f.getName());
			}
		}
		// --- FORMA 3: Filtro por prefijo y extension combinados ---

		/*--------------------------------------------------------
		    ya que tenemos un directorio creado lo usamos para 
		    crear el csv que no tenemos y filtrarlo 
		--------------------------------------------------------*/
		directorio = new File("datos");
		File ficheroCSV = new File("datos/alumno.csv");

		// Si no existe creame el csv

		try {
			if (!ficheroCSV.exists()) {
				ficheroCSV.createNewFile();
			}
		} catch (IOException e) {
			System.err.println("Error al crear el CSV: " + e.getMessage());
		}
		File[] alumnosCSV = directorio
				.listFiles((dir, nombre) -> nombre.startsWith("alumno") && nombre.endsWith(".csv"));
		System.out.println("CSVs de alumnos:");
		if (alumnosCSV != null && alumnosCSV.length > 0) {
			for (File f : alumnosCSV) {
				System.out.println(" " + f.getAbsolutePath());
			}
		} else {
			System.out.println(" (ninguno encontrado)");
		}

		// --- FORMA 4: Filtrar solo directorios con FileFilter ---

		// FileFilter recibe directamente el objeto File (no el nombre)
		// Util cuando necesitas llamar metodos como isDirectory() o length()

		// Para comprobarlo me voy al directorio src que contiene subdirectorios
		// y comprobar que funciona

		directorio = new File("src");
		File[] subdirectorios = directorio.listFiles(File::isDirectory);
		System.out.println("Subdirectorios:");
		if (subdirectorios != null) {
			for (File d : subdirectorios) {
				System.out.println(" [DIR] " + d.getName());
			}
		}
	}
}

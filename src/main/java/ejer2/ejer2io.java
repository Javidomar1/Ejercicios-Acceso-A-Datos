package ejer2;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class ejer2io {

	public static void main(String[] args) {

		// Generado con .io

		try (BufferedWriter bw = new BufferedWriter(
				new OutputStreamWriter(new FileOutputStream("datosio.txt"), StandardCharsets.UTF_8))) {

			bw.write("modulo,evaluacion,nota\n");
			bw.write("Acceso a datos,1º Evaluacion,8.5\n");
			bw.write("Programacion,2º Evaluacion,7.0\n");
			bw.write("Base de datos,1º Evaluacion,9.0\n");

			System.out.println("Archivo datos_io.csv generado correctamente.");

		} catch (IOException e) {
			System.out.println("Error de IO: " + e.getMessage());
		}
		System.out.println("--- Contenido del archivo ---");

		try (BufferedReader br = new BufferedReader(new FileReader("datos_io.csv"))) {
			String linea;
			while ((linea = br.readLine()) != null) {
				System.out.println(linea);
			}

		} catch (IOException e) {
			System.err.println("Error al leer: " + e.getMessage());
		}

	}

}

package ejer2;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Ejer2nio {

	public static void main(String[] args) {
		
		//Generado con .nio

		Path ruta = Paths.get("datos_nio.csv");
		List<String> datos = new ArrayList<>();
		datos.add("modulo,evaluacion,nota");
		datos.add("Acceso a datos,1º Evaluacion,8.5");
		datos.add("Programacion,2º Evaluacion,7.0");
		datos.add("Base de datos,1º Evaluacion,9.0");
		try {
			Files.write(ruta, datos);
			System.out.println("Archivo datos_nio.csv generado correctamente con NIO.");
		} catch (IOException e) {
			System.err.println("Error de escritura NIO: " + e.getMessage());
		}
	}

}

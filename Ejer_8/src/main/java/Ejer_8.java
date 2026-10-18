import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Stream;

public class Ejer_8 {
	private static Path RUTA= Paths.get("alumnos.txt");	
	public static void main(String[] args) throws IOException {

		Scanner sc = new Scanner(System.in);
		int opcion = 0;
		System.out.println("Escribe la opcion a elegir");
		do {
			System.out.println("""
					Elige una opción:
					1. Alta
					2. Mostrar
					3. Buscar
					4. Contar
					5. Salir
					""");
			
			System.out.print("Opción: ");
			opcion = sc.nextInt();
			sc.nextLine(); // Limpia el salto de línea
			
			// Hacemos 1 case por cada opción del menú usando números enteros
			switch (opcion) {
				case 1:
					System.out.print("Introduce los datos del alumno (Ej: Ana Garcia, 1 DAM): ");
					String datos = sc.nextLine();
					altaAlumnoPrueba(datos);
					break;
				case 2:
					System.out.println("\n--- Lista de Alumnos ---");
					Leer();
					break;
				case 3:
					if (!Files.exists(RUTA)) {
						System.out.println("""
							Antes de poder buscar tendra q existir el archivo
							Crealo manualmente o usa la opcion Alta
							""");
						break;
					}
					System.out.println("Escribe la palabra a buscar:");
					String encontrar = sc.nextLine();
					Buscar(encontrar);
					break;
				case 4:
					contarAlumnosPrueba();
					break;
				case 5:
					System.out.println("Saliendo del programa... ¡Hasta pronto!");
					break;
				default:
					System.out.println("Opción no válida. Por favor, elige un número del 1 al 5.");
			}
			
		} while (opcion != 5);
	}
	private static void altaAlumnoPrueba(String datos){
		try {
			Files.writeString(RUTA, datos + System.lineSeparator(), 
					StandardCharsets.UTF_8, 
					StandardOpenOption.CREATE, 
					StandardOpenOption.APPEND);
			System.out.println("Insertado: " + datos);
		} catch (IOException e) {
			System.err.println("Error al insertar el alumno: " + e.getMessage());
		}
		System.out.println();
    }
	private static void Leer(){
		
		// He elegido Stream en vez  RealAllLines si el archivo se 
		// ampliara aparte de ser mas corto el comando no carga todo en memoria
		
		try(Stream stream = Files.lines(RUTA, StandardCharsets.UTF_8)){
			stream.forEach(System.out::println);
		}catch (IOException e) {
			System.err.println("Error al insertar el alumno: " + e.getMessage());
		}
		System.out.println();
	}
	private static void Buscar(String buscado) {
		int contador = 0;
		try {
			// Lo guardamos en una lista una vez leido todas las lineas
			
			List <String> lineas = Files.readAllLines(RUTA,StandardCharsets.UTF_8);
			boolean encontrado = false;
			for (int i = 0; i < lineas.size(); i++) {
				String lineaActual = lineas.get(i);
				
				// Si la linea que buscamos es encontrada que la saque por pantalla 
				// y cambiamos que el estado de encontrado a true
				
				if (lineaActual.toLowerCase().contains(buscado.toLowerCase())) {
					System.out.println("!! Encontrado en Línea " + (i + 1) + ": " + lineaActual);
					contador++;
					encontrado = true;
				}
			}
			// Si no encontramos coincidencias que se imprima por pantalla
			if (!encontrado) {
	            System.out.println("No se encontraron coincidencias para: " + buscado);
	        }
		}catch (IOException e) {
			System.out.println("El error : " + e);
		}
		// por si encontrase una larga lista de nombres y quieres saber cuantos hay
		System.out.println("El numero de resultado de la busqueda: " + contador);
		System.out.println();
	}
	private static void contarAlumnosPrueba() {
		
		// Aqui un steam como el de leer pero en este caso con la opcion de contar
		
		try (Stream<String> stream = Files.lines(RUTA, StandardCharsets.UTF_8)) {
			long total = stream.count();
			System.out.println("Total de alumnos registrados: " + total);
		} catch (IOException e) {
			System.err.println("Error al contar los alumnos: " + e.getMessage());
		}
		System.out.println();
	}
	
}

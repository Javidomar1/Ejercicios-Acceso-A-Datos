
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

public class CrearRutas {

    public static void main(String[] args) {
        Path rutaArchivo = Paths.get("entrenamiento/registro.txt");

        // Creamos dos array uno con las carpetas y otras con el fichero
        String[] carpetas = {"src", "bin", "img", "src/junit"};
        String[] fichero = {"readme.md", "src/run.java", "img/imagen.png"};

        // Recorremos el Array con un bucle for el array de carpetas comprobamos si ya esta creado y si no esta creado lo creamos
        for (String ruta : carpetas) {
            Path dir = Paths.get(ruta);
            try {
                if (!Files.exists(dir)) {
                    Files.createDirectories(dir);
                    System.out.println("Se ha creado el directorio");
                } else {
                    System.out.println("El directorio ya existia");
                }
            } catch (Exception e) {
                System.out.println("Error al crear " + e.getMessage());
            }
        }

        //Hacemos lo mismo pero con el array de ficheros pero 
        for (String ruta : fichero) {
            Path file = Paths.get(ruta);
            try {
                if (!Files.exists(file)) {
                    Files.createFile(file);
                    System.out.println("Se ha creado el archivo!!");
                } else {
                    System.out.println("El archivo ya existia");
                }
            } catch (Exception e) {
                System.out.println("Error al crear " + e.getMessage());
            }
        }
        for (String ruta : carpetas) {
            Path dir = Paths.get(ruta);
            if (Files.exists(dir)) {
                try (Stream<Path> stream = Files.walk(dir)) {
                	stream.forEach(p -> System.out.println(" - " + p.toString()));
                } catch (IOException ex) {
                    Logger.getLogger(CrearRutas.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            // Como solo estabamos buscando en los ficheros y sus subdirectorios.
            // procedemos a ver si existe y si existe que me lo saques como 1 String
        }
        Path readme = Paths.get("readme.md");
        if (Files.exists(readme)) {
            System.out.println(" - " + readme.toString());
        }
    }
}

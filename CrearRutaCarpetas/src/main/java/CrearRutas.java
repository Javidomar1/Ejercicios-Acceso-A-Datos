
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CrearRutas {

    public static void main(String[] args) {
        Path rutaArchivo = Paths.get("entrenamiento/registro.txt");

        // Creamos dos array uno con las carpetas y otras con el fichero
        String[] carpetas = {"src", "bin", "img", "src/junit"};
        String[] fichero = {"readme.md", "src/run.java", "img/imagen.png"};

        // Recorremos el Array con un bucle for el array de carpetas
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

        //Hacemos lo mismo pero con el array de ficheros
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
            try (DirectoryStream<Path> stream = Files.walk(dir)) {
                for (Path p : stream) {
                    System.out.println(" - " + p.getFileName());
                }
            } catch (IOException ex) {
                Logger.getLogger(CrearRutas.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        //Recorremos otra vez los arrays pero dentro de for en vez de crearlos los comprobamos con Files.exits
        /*for (String comprobarCarp : carpetas) {
           boolean existe = false;
            Path dir = Paths.get(comprobarCarp);
            if (Files.exists(dir)) {
                System.out.println("La carpeta -> " + dir + "existe");
            } else {
                System.out.println("La carpeta -> " + dir + "no existe");
            }
        }
        //Recorremos el otro array comprobando otra vez si existen
        for (String comprobarFicheros : fichero) {
            Path fich = Paths.get(comprobarFicheros);
            if (Files.exists(fich)) {
                System.out.println("El fichero -> " + fich + "ya existe");
            } else {
                System.out.println("El fichero -> " + fich + "No existe");
            }

        }*/
    }
}

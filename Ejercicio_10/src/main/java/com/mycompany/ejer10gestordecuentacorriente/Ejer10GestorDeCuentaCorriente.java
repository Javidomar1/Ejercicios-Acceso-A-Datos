/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.mycompany.ejer10gestordecuentacorriente;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * PE Ejercicio 10. Gestor de cuenta corriente
 *
 * @author javie
 */
public class Ejer10GestorDeCuentaCorriente {
    // Nombre del archivo binario donde se guardará la información
    private static final String FICHERO = "cuentas.dat";
    //Longitud máxima del texto Al escribirse con writeChars, cada letra ocupará 2 bytes
    private static final int TAM_CONCEPTO = 20;
   //Tamaño exacto de cada apunte
    private static final int TAM_REGISTRO = 4 + 8 + TAM_CONCEPTO * 2 + 8;

    public static void main(String[] args) {
        // Apuntes de la imagen del enunciado
        ArrayList<Apunte> apuntes = new ArrayList<>();
        apuntes.add(new Apunte(1, LocalDate.of(2025, 1, 5), "Pago fra. teléfono", -91.57));
        apuntes.add(new Apunte(2, LocalDate.of(2025, 1, 10), "Ingreso en efectivo", 150.00));
        apuntes.add(new Apunte(3, LocalDate.of(2025, 1, 15), "Pago de préstamo", -392.61));
        apuntes.add(new Apunte(4, LocalDate.of(2025, 1, 20), "Cargo compra", -123.45));
        apuntes.add(new Apunte(5, LocalDate.of(2025, 1, 25), "Transf. a su favor", 721.27));
        apuntes.add(new Apunte(6, LocalDate.of(2025, 1, 31), "Reintegro cajero autom.", -80.00));

        Scanner sc = new Scanner(System.in);
        int opcion = -1;

        while (opcion != 0) {
            System.out.println("\n--- MENU CUENTAS ---");
            System.out.println("1. Insertar los apuntes iniciales en cuentas.dat");
            System.out.println("2. Mostrar todos los registros");
            System.out.println("3. Eliminar registro por id");
            System.out.println("4. Modificar concepto por id");
            System.out.println("5. Numero de registros y tamano del fichero");
            System.out.println("0. Salir");
            System.out.print("Elige opcion: ");

            try {
                opcion = Integer.parseInt(sc.nextLine().trim());

                switch (opcion) {
                    case 1 ->{
                        File f = new File(FICHERO);
                        if (f.exists() && f.length() > 0) {
                            System.out.println("Los apuntes iniciales ya están insertados en el fichero.");
                        } else {
                            insertar(apuntes);
                        }
                    }
                    case 2 ->
                        mostrar();
                    case 3 -> {
                        System.out.print("Id a eliminar: ");
                        int id = Integer.parseInt(sc.nextLine().trim());
                        System.out.println(eliminar(id) ? "Eliminado." : "No existe ese id.");
                    }
                    case 4 -> {
                        System.out.print("Id a modificar: ");
                        int id = Integer.parseInt(sc.nextLine().trim());
                        System.out.print("Nuevo concepto: ");
                        String concepto = sc.nextLine().trim();
                        System.out.println(modificar(id, concepto) ? "Modificado." : "No existe ese id o ya fue eliminado.");
                    }
                    case 5 ->
                        contar();
                    case 0 ->
                        System.out.println("Saliendo...");
                    default ->
                        System.out.println("Opcion no valida.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: Debes escribir un numero entero valido.");
            } catch (IOException e) {
                System.out.println("Error de fichero: " + e.getMessage());
            }
        }
        sc.close();
    }
    /**
     * Escribe un objeto Apunte en el fichero binario de acceso aleatorio.
     * Garantiza que el registro ocupe exactamente su tamaño fijo (60 bytes) 
     * forzando la longitud del concepto a 20 caracteres.
     * 
     * @param raf El flujo de acceso aleatorio (RandomAccessFile) donde se va a escribir.
     * @param a   El objeto Apunte que contiene los datos a persistir en el fichero.
     * @throws IOException Si ocurre un error de entrada/salida durante la escritura en el disco.
     */
    private static void escribir(RandomAccessFile raf, Apunte a) throws IOException {
        raf.writeInt(a.getId());
        raf.writeLong(a.getFecha().toEpochDay());
        StringBuffer sb = new StringBuffer(a.getConcepto());
        sb.setLength(TAM_CONCEPTO);
        raf.writeChars(sb.toString()); // 40 bytes (20 chars * 2)
        raf.writeDouble(a.getImporte());
    }
    /**
     * Inserta una lista de apuntes al final del fichero binario.
     * Si el fichero no existe, lo crea.
     * 
     * @param apuntes Un ArrayList con los objetos Apunte a guardar.
     * @throws IOException Si ocurre un error de escritura en disco.
     */
    private static void insertar(ArrayList<Apunte> apuntes) throws IOException {
        try (RandomAccessFile raf = new RandomAccessFile(FICHERO, "rw")) {
            raf.seek(raf.length());
            for (Apunte a : apuntes) {
                escribir(raf, a);
            }
            System.out.println(apuntes.size() + " apuntes insertados.");
        }
    }
    /**
     * Realiza una baja física de un apunte en el fichero binario según su ID.
     * Desplaza los registros inferiores hacia arriba para llenar el hueco
     * y recorta el tamaño final del archivo para eliminar el duplicado.
     * 
     * @param id El identificador único del apunte que se desea borrar.
     * @return true si el registro fue encontrado y eliminado, false si no existe.
     * @throws IOException Si ocurre un error de lectura/escritura o al cambiar el tamaño.
     */
    private static boolean eliminar(int id) throws IOException {
        try (RandomAccessFile raf = new RandomAccessFile(FICHERO, "rw")) {
            //Calculamos cuántos registros hay en total en el fichero
            long totalRegistros = raf.length() / TAM_REGISTRO;
            // Buscamos en qué posición está el ID usando tu método
            int pos = buscar(raf, id, totalRegistros);

            // Si buscar devuelve -1, significa que no existe
            if (pos == -1) {
                return false;
            }

            // desplazar hacia arriba
            for (int i = pos + 1; i < totalRegistros; i++) {
                // Saltamos al de abajo (i) y lo leemos
                raf.seek((long) i * TAM_REGISTRO);
                Apunte siguiente = leer(raf);

                // Saltamos al de arriba (i - 1) y escribimos el que acabamos de leer
                raf.seek((long) (i - 1) * TAM_REGISTRO);
                escribir(raf, siguiente);
            }

            //Cortamos el fichero quitando exactamente 1 registro (60 bytes) del final
            raf.setLength((totalRegistros - 1) * TAM_REGISTRO);

            return true;
        }
        // 3. Cortamos el registro duplicado que sobra al final del fichero
        //O poniendo a 0 la posicion de bytes del id correspondiente
        // de tal forma que cuando intente leerlo no lo vera 
        // Evita calcular posiciones negativas
        /*long pos = (long) (id - 1) * TAM_REGISTRO;
            if (pos < raf.length()) {
                raf.seek(pos);
                raf.writeInt(0);
                return true;
            }*/
    }
    /**
     * Lee secuencialmente todos los registros del fichero binario
     * y los imprime por consola. Si el fichero no existe, informa de ello.
     * 
     * @throws IOException Si ocurre un error al intentar leer el fichero.
     */
    private static void mostrar() throws IOException {
        File f = new File(FICHERO);
        // Verificamos si el archivo existe y tiene datos antes de intentar leer
        if (!f.exists() || f.length() == 0) {
            System.out.println("El fichero está vacío o no existe.");
            return;
        }

        try (RandomAccessFile raf = new RandomAccessFile(FICHERO, "r")) {
            long totalRegistros = raf.length() / TAM_REGISTRO;
            System.out.println("\n--- LISTADO DE APUNTES ---");
            
            for (int i = 0; i < totalRegistros; i++) {
                // Saltamos a la posición de inicio de cada bloque de 60 bytes
                raf.seek((long) i * TAM_REGISTRO);
                Apunte a = leer(raf);
                
                // Imprimimos el objeto 
                System.out.println(a.toString());
            }
        }
    }
    /**
     * Modifica exclusivamente el campo 'concepto' de un apunte existente
     * utilizando acceso aleatorio directo al byte correspondiente.
     * 
     * @param id            El identificador del apunte a modificar.
     * @param nuevoConcepto El nuevo texto que reemplazará al concepto anterior (máx 20 caracteres).
     * @return true si se actualizó correctamente, false si el ID no existe.
     * @throws IOException Si ocurre un error de posicionamiento o escritura en disco.
     */
    private static boolean modificar(int id, String nuevoConcepto) throws IOException {
        try (RandomAccessFile raf = new RandomAccessFile(FICHERO, "rw")) {
            long totalRegistros = raf.length() / TAM_REGISTRO;
            int pos = buscar(raf, id, totalRegistros);

            if (pos == -1) {
                return false; // El ID no existe
            }

            // Saltamos a la casilla del registro + 12 bytes para saltarnos el ID (4) y la fecha (8)
            raf.seek((long) pos * TAM_REGISTRO + 12);

            // Preparamos el nuevo concepto forzando los 20 caracteres exactos para no romper los 60 bytes
            StringBuffer sb = new StringBuffer(nuevoConcepto);
            sb.setLength(TAM_CONCEPTO);
            
            // Sobrescribimos exactamente 40 bytes en disco
            raf.writeChars(sb.toString()); 
            
            return true;
        }
    }
    /**
     * Calcula y muestra por pantalla el tamaño total del fichero en bytes
     * y el número exacto de apuntes que contiene.
     * 
     * @throws IOException Si hay problemas al acceder a los metadatos del fichero.
     */
    private static void contar() throws IOException {
        File f = new File(FICHERO);
        if (!f.exists()) {
            System.out.println("El fichero no existe aún (0 bytes).");
            return;
        }

        try (RandomAccessFile raf = new RandomAccessFile(FICHERO, "r")) {
            long bytesTotales = raf.length();
            // Dividir el total entre 60 da el recuento exacto gracias al tamaño fijo
            long totalRegistros = bytesTotales / TAM_REGISTRO;
            
            System.out.println("\n--- ESTADÍSTICAS DEL FICHERO ---");
            System.out.println("Tamaño total del fichero: " + bytesTotales + " bytes");
            System.out.println("Número de registros almacenados: " + totalRegistros);
        }
    }
    /**
     * Método auxiliar para buscar la posición secuencial de un ID
     */
    private static int buscar(RandomAccessFile raf, int id, long totalRegistros) throws IOException {
        // Un bucle for segun el total de registros que le pases
        for (int i = 0; i < totalRegistros; i++) {
            // Mueve el puntero de lectura al comienzo byte a byte de la casilla.
            raf.seek((long) i * TAM_REGISTRO);
            //Como los 1º son id en cada registro con readInt lee ese numero 
            //el puntero avanza 4 bytes directamente
            if (raf.readInt() == id) {
                return i;
            }
        }
        //si no existe devuelve -1
        return -1;
    }
    /**
     * Reconstruye un objeto Apunte leyendo sus 60 bytes secuencialmente 
     * a partir de la posición actual del puntero en el fichero.
     * 
     * @param raf El flujo binario desde donde se van a leer los datos.
     * @return Un objeto Apunte instanciado con los valores recuperados del disco.
     * @throws IOException Si se intenta leer más allá del final del archivo o falla el acceso.
     */
    private static Apunte leer(RandomAccessFile raf) throws IOException {
        int id = raf.readInt();
        long epoch = raf.readLong();
        // Para reconstruir un String que fue escrito con writeChars()
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < TAM_CONCEPTO; i++) {
            sb.append(raf.readChar());// Lee 40 bytes (20 chars x 2 bytes)
        }
        
        double importe = raf.readDouble();
        //Usamos trim() para eliminar los espacios nulo
        return new Apunte(id, LocalDate.ofEpochDay(epoch), sb.toString().trim(), importe);
    }
}

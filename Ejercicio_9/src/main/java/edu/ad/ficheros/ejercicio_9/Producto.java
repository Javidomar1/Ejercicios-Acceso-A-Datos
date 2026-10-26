package edu.ad.ficheros.ejercicio_9;

public class Producto {
    // Atributos con la primera letra en mayúscula (y corregido el error de "recio")
    private int ID;
    private String Nombre;
    private double Precio;

    public Producto(int id, String nombre, double precio) {
        // Validaciones recuperadas de tu versión anterior
        if (id <= 0) {
            throw new IllegalArgumentException("El ID debe ser mayor que 0");
        }
        if (nombre == null || nombre.isEmpty()) {
            throw new IllegalArgumentException("Debe tener nombre");
        }
        if (precio < 0) {
            throw new IllegalArgumentException("El precio no puede ser negativo");
        }
        
        // Asignación correcta: el atributo en mayúscula recibe el parámetro en minúscula
        this.ID = id;
        this.Nombre = nombre;
        this.Precio = precio;
    }

    public int getID() {
        return ID;
    }

    public void setID(int id) {
        this.ID = id;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        this.Nombre = nombre;
    }

    public double getPrecio() {
        return Precio;
    }

    public void setPrecio(double precio) {
        this.Precio = precio;
    }

    @Override
    public String toString() {
        return "ID: " + ID + " | Nombre: " + Nombre + " | Precio: " + Precio;
    }
}
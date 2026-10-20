/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.ad.ficheros.ejercicio_9;

/**
 *
 * @author javier.dommar
 */
public class Producto {
    private  int ID;
    private  String nombre;
    private  double precio;

    public Producto(int ID, String nombre, double precio) {
        if (ID < 0){
            throw new IllegalArgumentException("El ID debe ser mayor que 0");
        }
        if(nombre.isEmpty() || nombre == null){
            throw new IllegalArgumentException("Debe tener nombre");
        }
        if(precio < 0){
            throw new IllegalArgumentException("El precio no puede ser negativo");
        }
        this.ID = ID;
        this.nombre = nombre;
        this.precio = precio;
    }
    
}

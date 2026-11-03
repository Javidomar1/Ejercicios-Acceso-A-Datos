package com.mycompany.ejer10gestordecuentacorriente;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

import java.time.LocalDate;
 
/**
 * Representa un apunte bancario (una fila de la cuenta).
 *
 * <p>Es un objeto inmutable: para "modificar" un apunte se crea otro nuevo.</p>
 */
public class Apunte {
 
    private final int id;
    private final LocalDate fecha;
    private final String concepto;
    private final double importe;
 

    public Apunte(int id, LocalDate fecha, String concepto, double importe) {
        this.id = id;
        this.fecha = fecha;
        this.concepto = concepto;
        this.importe = importe;
    }
 
    public int getId() {
        return id;
    }
 
    public LocalDate getFecha() {
        return fecha;
    }
 
    public String getConcepto() {
        return concepto;
    }
 
    public double getImporte() {
        return importe;
    }
 
    @Override
    public String toString() {
        return String.format("ID: %d | Fecha: %s | Concepto: %-20s | Importe: %10.2f",
                id, fecha, concepto, importe);
    }
}
 

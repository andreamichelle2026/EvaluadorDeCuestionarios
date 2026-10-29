/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.amcm.evaluadordecuestionarios;

import java.util.List;

/** Concepto de la rubrica de una pregunta abierta: nombre, peso y sinonimos que lo cubren. */
public final class Concepto {

    private final String nombre;
    private final double peso;
    private final List<String> sinonimos;

    public Concepto(String nombre, double peso, String... sinonimos) {
        this.nombre = nombre;
        this.peso = peso;
        this.sinonimos = List.of(sinonimos);
    }

    public String getNombre() { return nombre; }
    public double getPeso() { return peso; }
    public List<String> getSinonimos() { return sinonimos; }
}

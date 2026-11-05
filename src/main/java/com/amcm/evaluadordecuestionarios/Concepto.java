/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.amcm.evaluadordecuestionarios;

import java.util.List;

/*
 * Clase de datos que representa los valores para la rubrica de una pregunta larga. 
 * Los campos se asignan una sola vez y solo se leen mediante los getters
 * la clase ProcesadorConceptos (Scala) la consulta.
 */

public final class Concepto {

    private final String nombre; // Descripción legible del concepto
    private final double peso; // Cuánto vale ese concepto en la nota
    private final List<String> sinonimos; // Palabras o frases que son parte de la respuesta

    public Concepto(String nombre, double peso, String... sinonimos) {
        this.nombre = nombre;
        this.peso = peso;
        this.sinonimos = List.of(sinonimos); // no se puede añadi ni quitar sinónimos desde fuera
    }

    public String getNombre() { return nombre; }
    public double getPeso() { return peso; }
    public List<String> getSinonimos() { return sinonimos; }
}

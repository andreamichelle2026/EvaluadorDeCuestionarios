/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.amcm.evaluadordecuestionarios;

import java.util.List;

/** Pregunta del cuestionario: corta (respuestas aceptadas) o larga (rubrica de conceptos). */
public final class Pregunta {

    private final int id;
    private final String enunciado;
    private final TipoPregunta tipo;
    private final List<String> respuestasAceptadas;
    private final List<Concepto> conceptos;

    private Pregunta(int id, String enunciado, TipoPregunta tipo,
                     List<String> respuestasAceptadas, List<Concepto> conceptos) {
        this.id = id;
        this.enunciado = enunciado;
        this.tipo = tipo;
        this.respuestasAceptadas = respuestasAceptadas;
        this.conceptos = conceptos;
    }

    public static Pregunta corta(int id, String enunciado, String... aceptadas) {
        return new Pregunta(id, enunciado, TipoPregunta.CORTA, List.of(aceptadas), List.of());
    }

    public static Pregunta larga(int id, String enunciado, Concepto... conceptos) {
        return new Pregunta(id, enunciado, TipoPregunta.LARGA, List.of(), List.of(conceptos));
    }

    public int getId() { return id; }
    public String getEnunciado() { return enunciado; }
    public TipoPregunta getTipo() { return tipo; }
    public List<String> getRespuestasAceptadas() { return respuestasAceptadas; }
    public List<Concepto> getConceptos() { return conceptos; }
}
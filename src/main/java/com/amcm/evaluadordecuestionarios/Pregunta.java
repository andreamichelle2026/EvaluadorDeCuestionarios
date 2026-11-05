/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.amcm.evaluadordecuestionarios;

import java.util.List;

/**
 * Clase de datos que representa una pregunta del cuestionario. 
 * Guarda su enunciado y los datos que cada plugin necesita para evaluarla. 
 * Hay tres tipos de pregunta, pero una sola clase.
 */

public final class Pregunta {

    private final int id;
    private final String enunciado;
    private final TipoPregunta tipo;
    private final List<String> respuestasAceptadas; // CORTAS
    private final List<Concepto> conceptos;     // LARGAS
    private final List<String> opciones;        // ALTERNATIVAS
    private final char respuestaCorrecta;       // ALTERNATIVAS ('A'..'D'), '\0' en otro caso

    private Pregunta(int id, String enunciado, TipoPregunta tipo,
                     List<String> respuestasAceptadas, List<Concepto> conceptos,
                     List<String> opciones, char respuestaCorrecta) {
        this.id = id;
        this.enunciado = enunciado;
        this.tipo = tipo;
        this.respuestasAceptadas = respuestasAceptadas;
        this.conceptos = conceptos;
        this.opciones = opciones;
        this.respuestaCorrecta = respuestaCorrecta;
    }

    /** Método para la respuesta corta */
    public static Pregunta corta(int id, String enunciado, String... aceptadas) {
        return new Pregunta(id, enunciado, TipoPregunta.CORTA,
                List.of(aceptadas), List.of(), List.of(), '\0');
    }
    /** Método para la respuesta larga */
    public static Pregunta larga(int id, String enunciado, Concepto... conceptos) {
        return new Pregunta(id, enunciado, TipoPregunta.LARGA,
                List.of(), List.of(conceptos), List.of(), '\0');
    }

    /** Método para respuesta de alternativas: las opciones se rotulan A, B, C, D en el orden dado. */
    public static Pregunta alternativas(int id, String enunciado, char correcta, String... opciones) {
        if (opciones.length < 2 || opciones.length > 4) {
            throw new IllegalArgumentException("Una pregunta de alternativas necesita entre 2 y 4 opciones");
        }
        char c = Character.toUpperCase(correcta);
        if (c < 'A' || c >= 'A' + opciones.length) {
            throw new IllegalArgumentException("La respuesta correcta '" + correcta + "' no corresponde a ninguna opcion");
        }
        return new Pregunta(id, enunciado, TipoPregunta.ALTERNATIVAS,
                List.of(), List.of(), List.of(opciones), c);
    }

    public int getId() { return id; }
    public String getEnunciado() { return enunciado; }
    public TipoPregunta getTipo() { return tipo; }
    public List<String> getRespuestasAceptadas() { return respuestasAceptadas; }
    public List<Concepto> getConceptos() { return conceptos; }
    public List<String> getOpciones() { return opciones; }
    public char getRespuestaCorrecta() { return respuestaCorrecta; }
}
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.amcm.evaluadordecuestionarios;

/** Lo que el monitor observa: tamanio de la respuesta, tipo de pregunta y la pregunta. */
public class Contexto {
    private final int tamanioTexto;
    private final TipoPregunta tipo;
    private final Pregunta pregunta;   // pregunta que se esta evaluando (puede ser null)

    public Contexto(int tamanioTexto, TipoPregunta tipo, Pregunta pregunta) {
        this.tamanioTexto = tamanioTexto;
        this.tipo = tipo;
        this.pregunta = pregunta;
    }

    public int getTamanioTexto() { return tamanioTexto; }
    public TipoPregunta getTipo() { return tipo; }
    public Pregunta getPregunta() { return pregunta; }
}

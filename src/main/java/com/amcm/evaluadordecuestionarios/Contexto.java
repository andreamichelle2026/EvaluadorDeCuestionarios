/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.amcm.evaluadordecuestionarios;

public class Contexto {
    private int tamanioTexto;
    private boolean modoValidacion;
    private Pregunta pregunta;   // pregunta que se esta evaluando (puede ser null)

    public Contexto(int tamanioTexto, boolean modoValidacion) {
        this(tamanioTexto, modoValidacion, null);
    }

    public Contexto(int tamanioTexto, boolean modoValidacion, Pregunta pregunta) {
        this.tamanioTexto = tamanioTexto;
        this.modoValidacion = modoValidacion;
        this.pregunta = pregunta;
    }

    public int getTamanioTexto() { return tamanioTexto; }
    public boolean isModoValidacion() { return modoValidacion; }
    public Pregunta getPregunta() { return pregunta; }
}
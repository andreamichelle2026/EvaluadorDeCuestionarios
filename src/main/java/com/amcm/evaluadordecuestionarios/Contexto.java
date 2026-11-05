/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.amcm.evaluadordecuestionarios;

/*
 * Clase de datos que representa la información de la pregunta que se está evaluando,
 * para pasársela al selector y a los plugins.
*/

public class Contexto {
    private int tamanioTexto; // Longitud en caracteres de la respuesta del usuario
    private boolean modoValidacion; // true si la pregunta es tipo CORTA / ALTERNATIVAS
    private Pregunta pregunta;   // pregunta que se esta evaluando (puede ser null)

    public Contexto(int tamanioTexto, boolean modoValidacion, Pregunta pregunta) {
        this.tamanioTexto = tamanioTexto;
        this.modoValidacion = modoValidacion;
        this.pregunta = pregunta;
    }

    public int getTamanioTexto() { return tamanioTexto; }
    public boolean isModoValidacion() { return modoValidacion; }
    public Pregunta getPregunta() { return pregunta; }

    /** true si la pregunta evaluada es de alternativas (A, B, C, D). */
    public boolean isAlternativas() {
        return pregunta != null && pregunta.getTipo() == TipoPregunta.ALTERNATIVAS;
    }
}
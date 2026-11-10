/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.amcm.evaluadordecuestionarios;

import java.util.Objects;

/**
 * Monitor: observa la situacion actual y construye el Contexto.
 *
 * Observa dos cosas:
 *  1) el tamanio de la respuesta (cantidad de letras, sin espacios, signos ni digitos);
 *  2) el tipo de la pregunta (CORTA, ALTERNATIVAS o LARGA).
 *
 * El monitor solo MIDE. La decision del plugin a usar, la toma SelectorPlugin.
 */
public final class MonitorTexto {

    private MonitorTexto() {}

    public static Contexto observar(String entrada, Pregunta pregunta) {
        Objects.requireNonNull(pregunta, "el monitor necesita la pregunta que se evalua");
        int letras = contarLetras(entrada);
        TipoPregunta tipo = pregunta.getTipo();
        return new Contexto(letras, tipo, pregunta);
    }

    /** Cuenta solo letras: ignora espacios, signos de puntuacion y digitos. */
    static int contarLetras(String texto) {
        if (texto == null) {
            return 0;
        }
        int letras = 0;
        for (int i = 0; i < texto.length(); i++) {
            if (Character.isLetter(texto.charAt(i))) {
                letras++;
            }
        }
        return letras;
    }
}

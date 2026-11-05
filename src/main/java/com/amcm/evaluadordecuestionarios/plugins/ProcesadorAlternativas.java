package com.amcm.evaluadordecuestionarios.plugins;

import com.amcm.evaluadordecuestionarios.Contexto;
import com.amcm.evaluadordecuestionarios.Pregunta;
import com.amcm.evaluadordecuestionarios.ProcesadorPlugin;
import com.amcm.evaluadordecuestionarios.Resultado;
import com.amcm.evaluadordecuestionarios.TextoUtil;

/**
 * Plugin Java para preguntas de alternativas (A, B, C, D).
 * Acepta "b", "B", "b)", "opcion b" o "respuesta b". Cualquier otra entrada
 * (varias letras, letra fuera de rango, vacia) se considera no valida y puntua 0.
 */
public class ProcesadorAlternativas implements ProcesadorPlugin {

    @Override
    public String id() {
        return "alternativas";
    }

    @Override
    public boolean soporta(Contexto contexto) {
        return contexto.isAlternativas();
    }

    @Override
    public Resultado procesar(String entrada) {
        return new Resultado("Sin pregunta asociada: no se puede evaluar la alternativa", 0.0);
    }

    @Override
    public Resultado procesar(String entrada, Contexto contexto) {
        Pregunta pregunta = contexto.getPregunta();
        if (pregunta == null || !contexto.isAlternativas()) {
            return procesar(entrada);
        }

        char correcta = pregunta.getRespuestaCorrecta();
        int numOpciones = pregunta.getOpciones().size();
        Character elegida = extraerLetra(entrada, numOpciones);

        if (elegida == null) {
            char ultima = (char) ('A' + numOpciones - 1);
            return new Resultado("Respuesta no valida: escribe una letra de A a " + ultima
                    + " (correcta: " + descripcion(pregunta, correcta) + ")", 0.0);
        }
        if (elegida == correcta) {
            return new Resultado("Respuesta correcta (" + descripcion(pregunta, correcta) + ")", 1.0);
        }
        return new Resultado("Respuesta incorrecta: elegiste " + elegida
                + " (correcta: " + descripcion(pregunta, correcta) + ")", 0.0);
    }

    /** Devuelve la letra mayuscula elegida o null si la entrada no es una letra valida. */
    private Character extraerLetra(String entrada, int numOpciones) {
        String n = TextoUtil.normalizar(entrada);
        if (n.startsWith("opcion ")) {
            n = n.substring("opcion ".length());
        } else if (n.startsWith("respuesta ")) {
            n = n.substring("respuesta ".length());
        }
        if (n.length() != 1) {
            return null;
        }
        char letra = Character.toUpperCase(n.charAt(0));
        return (letra >= 'A' && letra < 'A' + numOpciones) ? Character.valueOf(letra) : null;
    }

    private String descripcion(Pregunta pregunta, char letra) {
        return letra + ") " + pregunta.getOpciones().get(letra - 'A');
    }
}

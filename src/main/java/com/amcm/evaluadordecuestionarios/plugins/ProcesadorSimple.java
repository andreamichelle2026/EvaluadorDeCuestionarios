package com.amcm.evaluadordecuestionarios.plugins;

import com.amcm.evaluadordecuestionarios.Contexto;
import com.amcm.evaluadordecuestionarios.Pregunta;
import com.amcm.evaluadordecuestionarios.ProcesadorPlugin;
import com.amcm.evaluadordecuestionarios.Resultado;
import com.amcm.evaluadordecuestionarios.TextoUtil;
import com.amcm.evaluadordecuestionarios.SelectorPlugin;

/**
 * Plugin Java para preguntas de respuesta corta (modo validacion):
 * normaliza la respuesta y la compara con las respuestas aceptadas.
 */
public class ProcesadorSimple implements ProcesadorPlugin {

    @Override
    public String id() {
        return "simple";
    }

    @Override
    public boolean soporta(Contexto contexto) {
        if (contexto.isAlternativas()) return false;
        return contexto.isModoValidacion()
            || contexto.getTamanioTexto() < SelectorPlugin.TEXTO_MINIMO_ABIERTA;
    }

    /** cuenta palabras (se usa si no hay pregunta asociada). */
    @Override
    public Resultado procesar(String entrada) {
        String[] palabras = entrada.trim().split("\\s+");
        return new Resultado("Palabras: " + palabras.length, 0.0);
    }

    @Override
    public Resultado procesar(String entrada, Contexto contexto) {
        Pregunta pregunta = contexto.getPregunta();
        if (pregunta == null) {
            return procesar(entrada);
        }
        if (pregunta.getRespuestasAceptadas().isEmpty()) {   // pregunta abierta
            return new Resultado("Respuesta demasiado corta (" + contexto.getTamanioTexto()
                + " caracteres, minimo " + SelectorPlugin.TEXTO_MINIMO_ABIERTA
                + "): no hay texto para evaluar la rubrica", 0.0);
        }
        String dada = TextoUtil.normalizar(entrada);
        if (!dada.isEmpty()) {
            for (String aceptada : pregunta.getRespuestasAceptadas()) {
                if (dada.equals(TextoUtil.normalizar(aceptada))) {
                    return new Resultado("Respuesta correcta", 1.0);
                }
            }
        }
        return new Resultado("Respuesta incorrecta (esperada: "
                + pregunta.getRespuestasAceptadas().get(0) + ")", 0.0);
    }
}

package com.amcm.evaluadordecuestionarios.plugins;

import com.amcm.evaluadordecuestionarios.Contexto;
import com.amcm.evaluadordecuestionarios.PluginBase;
import com.amcm.evaluadordecuestionarios.Pregunta;
import com.amcm.evaluadordecuestionarios.Resultado;
import com.amcm.evaluadordecuestionarios.SelectorPlugin;
import com.amcm.evaluadordecuestionarios.TextoUtil;
import com.amcm.evaluadordecuestionarios.TipoPregunta;

import java.util.List;

/**
 * Plugin Java para preguntas de respuesta corta: normaliza la respuesta y la
 * compara con las respuestas aceptadas. Tambien atiende las abiertas con
 * muy poco texto, que rechaza sin cargar el plugin de conceptos.
 */
public class ProcesadorSimple extends PluginBase {

    public ProcesadorSimple() {
        super("simple");
    }

    @Override
    public boolean soporta(Contexto contexto) {
        TipoPregunta tipo = contexto.getTipo();
        return tipo == TipoPregunta.CORTA
                || (tipo == TipoPregunta.LARGA
                    && contexto.getTamanioTexto() < SelectorPlugin.TEXTO_MINIMO_ABIERTA);
    }

    /** Sin pregunta asociada: solo cuenta palabras. */
    @Override
    public Resultado procesar(String entrada) {
        int palabras = (entrada == null || entrada.isBlank()) ? 0 : entrada.trim().split("\\s+").length;
        return new Resultado("Palabras: " + palabras, 0.0);
    }

    @Override
    protected Resultado evaluar(String entrada, Pregunta pregunta, Contexto contexto) {
        List<String> aceptadas = pregunta.getRespuestasAceptadas();

        if (aceptadas.isEmpty()) {   // pregunta abierta con texto insuficiente
            return new Resultado("Respuesta demasiado corta (" + contexto.getTamanioTexto()
                    + " letras, minimo " + SelectorPlugin.TEXTO_MINIMO_ABIERTA
                    + "): no hay texto para evaluar la rubrica", 0.0);
        }

        String dada = TextoUtil.normalizar(entrada);
        boolean correcta = !dada.isEmpty()
                && aceptadas.stream()
                            .map(TextoUtil::normalizar)
                            .anyMatch(dada::equals);

        return correcta
                ? new Resultado("Respuesta correcta", 1.0)
                : new Resultado("Respuesta incorrecta (esperada: " + aceptadas.get(0) + ")", 0.0);
    }
}

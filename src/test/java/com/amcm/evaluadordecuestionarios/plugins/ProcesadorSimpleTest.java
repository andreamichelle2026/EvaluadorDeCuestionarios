package com.amcm.evaluadordecuestionarios.plugins;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.amcm.evaluadordecuestionarios.Contexto;
import com.amcm.evaluadordecuestionarios.Pregunta;
import com.amcm.evaluadordecuestionarios.ProcesadorPlugin;
import org.junit.jupiter.api.Test;

class ProcesadorSimpleTest {

    private final ProcesadorPlugin plugin = new ProcesadorSimple();
    private final Pregunta pregunta =
            Pregunta.corta(1, "Que palabra clave declara una constante?", "final");

    private double nota(String respuesta) {
        Contexto contexto = new Contexto(respuesta.length(), true, pregunta);
        return plugin.procesar(respuesta, contexto).getNota();
    }

    @Test
    void respuestaCorrectaDa1() {
        assertEquals(1.0, nota("final"), 1e-9);
    }

    @Test
    void ignoraMayusculasYEspacios() {
        assertEquals(1.0, nota("  FINAL "), 1e-9);
    }

    @Test
    void respuestaIncorrectaDa0() {
        assertEquals(0.0, nota("const"), 1e-9);
    }

    @Test
    void respuestaVaciaDa0() {
        assertEquals(0.0, nota(""), 1e-9);
    }

    @Test
    void soloSoportaModoValidacion() {
        assertTrue(plugin.soporta(new Contexto(5, true)));
        assertFalse(plugin.soporta(new Contexto(5, false)));
    }
}

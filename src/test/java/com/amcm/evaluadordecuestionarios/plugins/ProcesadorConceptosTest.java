package com.amcm.evaluadordecuestionarios.plugins;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.amcm.evaluadordecuestionarios.BancoPreguntas;
import com.amcm.evaluadordecuestionarios.Concepto;
import com.amcm.evaluadordecuestionarios.Contexto;
import com.amcm.evaluadordecuestionarios.Pregunta;
import com.amcm.evaluadordecuestionarios.ProcesadorPlugin;
import org.junit.jupiter.api.Test;

/** Prueba el plugin escrito en Scala desde una clase de prueba Java. */
class ProcesadorConceptosTest {

    private final ProcesadorPlugin plugin = new ProcesadorConceptos();

    private final Pregunta herencia = Pregunta.larga(1, "Que es la herencia?",
            new Concepto("subclase", 1.0, "subclase", "clase hija"),
            new Concepto("superclase", 1.0, "superclase", "clase padre"));

    private double nota(Pregunta pregunta, String respuesta) {
        Contexto contexto = new Contexto(respuesta.length(), false, pregunta);
        return plugin.procesar(respuesta, contexto).getNota();
    }

    @Test
    void conTodosLosConceptosDa1() {
        assertEquals(1.0, nota(herencia, "Una subclase hereda de una superclase"), 1e-9);
    }

    @Test
    void conUnConceptoDaLaMitad() {
        assertEquals(0.5, nota(herencia, "La clase hija reutiliza codigo"), 1e-9);
    }

    @Test
    void sinConceptosDa0() {
        assertEquals(0.0, nota(herencia, "No lo se"), 1e-9);
    }

    @Test
    void sinonimoLargoValeComoPrefijoDePalabra() {
        Pregunta objeto = Pregunta.larga(2, "Que es un objeto?",
                new Concepto("objeto como instancia", 1.0, "instanci"));
        assertEquals(1.0, nota(objeto, "Un objeto es una instancia de la clase"), 1e-9);
    }

    @Test
    void sinonimoCortoDebeSerPalabraExacta() {
        Pregunta bucle = Pregunta.larga(3, "Cuando se usa for?",
                new Concepto("bucle for", 1.0, "for"));
        assertEquals(0.0, nota(bucle, "informacion formal"), 1e-9);
        assertEquals(1.0, nota(bucle, "se usa un for"), 1e-9);
    }

    @Test
    void preguntaDelBancoConCreditoParcial() {
        Pregunta p9 = BancoPreguntas.todas().get(8);
        assertEquals(9, p9.getId());
        String respuesta = "La herencia permite que una clase hija reutilice el código de otra clase.";
        assertEquals(2.0 / 3.0, nota(p9, respuesta), 1e-9);
    }

    @Test
    void soloSoportaPreguntasSinModoValidacion() {
        assertTrue(plugin.soporta(new Contexto(80, false)));
        assertFalse(plugin.soporta(new Contexto(5, true)));
    }
}

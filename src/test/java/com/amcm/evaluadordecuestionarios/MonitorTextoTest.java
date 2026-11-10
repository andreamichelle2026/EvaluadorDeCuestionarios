package com.amcm.evaluadordecuestionarios;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MonitorTextoTest {

    private static final Pregunta CORTA = Pregunta.corta(1, "Palabra clave de constante?", "final");
    private static final Pregunta ALTERNATIVAS = Pregunta.alternativas(2, "Cual es primitivo?", 'A', "int", "String");
    private static final Pregunta LARGA = Pregunta.larga(3, "Explica una clase.", new Concepto("plantilla", 1.0, "plantilla"));

    // ---------- contarLetras ----------

    @Test
    @DisplayName("contarLetras ignora espacios, signos y digitos")
    void contarLetrasIgnoraLoQueNoSonLetras() {
        assertEquals(3, MonitorTexto.contarLetras("  ab 1 2 ,c! "));
    }

    @Test
    @DisplayName("contarLetras cuenta letras con tilde y la enie")
    void contarLetrasCuentaTildesYEnie() {
        assertEquals(4, MonitorTexto.contarLetras("\u00e1\u00e9\u00ed \u00f1"));
    }

    @Test
    @DisplayName("contarLetras devuelve 0 con null, vacio o solo signos")
    void contarLetrasSinLetras() {
        assertEquals(0, MonitorTexto.contarLetras(null));
        assertEquals(0, MonitorTexto.contarLetras(""));
        assertEquals(0, MonitorTexto.contarLetras("?????????? 123 !!"));
    }

    // ---------- observar ----------

    @Test
    @DisplayName("observar guarda el numero de letras, no de caracteres")
    void observarGuardaLetras() {
        Contexto contexto = MonitorTexto.observar("..........   ", LARGA);
        assertEquals(0, contexto.getTamanioTexto());

        contexto = MonitorTexto.observar("una clase", LARGA);
        assertEquals(8, contexto.getTamanioTexto());
    }

    @Test
    @DisplayName("observar copia el tipo de la pregunta (corta, alternativas, larga)")
    void observarCopiaElTipo() {
        assertEquals(TipoPregunta.CORTA, MonitorTexto.observar("final", CORTA).getTipo());
        assertEquals(TipoPregunta.ALTERNATIVAS, MonitorTexto.observar("A", ALTERNATIVAS).getTipo());
        assertEquals(TipoPregunta.LARGA, MonitorTexto.observar("texto", LARGA).getTipo());
    }

    @Test
    @DisplayName("observar guarda la misma pregunta en el contexto")
    void observarGuardaLaPregunta() {
        assertSame(CORTA, MonitorTexto.observar("final", CORTA).getPregunta());
    }

    @Test
    @DisplayName("observar con una respuesta null cuenta 0 letras")
    void observarConEntradaNula() {
        assertEquals(0, MonitorTexto.observar(null, CORTA).getTamanioTexto());
    }

    @Test
    @DisplayName("observar sin pregunta lanza NullPointerException")
    void observarSinPreguntaFalla() {
        assertThrows(NullPointerException.class, () -> MonitorTexto.observar("hola", null));
    }
}

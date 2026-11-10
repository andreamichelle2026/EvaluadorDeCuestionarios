package com.amcm.evaluadordecuestionarios.plugins;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.amcm.evaluadordecuestionarios.Concepto;
import com.amcm.evaluadordecuestionarios.Contexto;
import com.amcm.evaluadordecuestionarios.Pregunta;
import com.amcm.evaluadordecuestionarios.Resultado;
import com.amcm.evaluadordecuestionarios.TipoPregunta;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class ProcesadorAlternativasTest {

    private final ProcesadorAlternativas plugin = new ProcesadorAlternativas();

    /** Cuatro opciones, la correcta es la C. */
    private final Pregunta cuatroOpciones = Pregunta.alternativas(1, "Cual NO es primitivo?", 'C',
            "int", "boolean", "String", "double");

    private static Contexto ctx(Pregunta pregunta, int letras) {
        return new Contexto(letras, pregunta.getTipo(), pregunta);
    }

    private Resultado responder(String respuesta) {
        return plugin.procesar(respuesta, ctx(cuatroOpciones, respuesta == null ? 0 : respuesta.length()));
    }

    // ---------- Identidad y soporte ----------

    @Test
    @DisplayName("id es 'alternativas'")
    void idEsAlternativas() {
        assertEquals("alternativas", plugin.id());
    }

    @Test
    @DisplayName("solo soporta las preguntas de alternativas")
    void soloSoportaAlternativas() {
        Pregunta corta = Pregunta.corta(2, "P?", "x");
        Pregunta larga = Pregunta.larga(3, "P?", new Concepto("c", 1.0, "c"));

        assertTrue(plugin.soporta(ctx(cuatroOpciones, 1)));
        assertFalse(plugin.soporta(ctx(corta, 1)));
        assertFalse(plugin.soporta(ctx(larga, 1)));
    }

    // ---------- Evaluacion ----------

    @ParameterizedTest
    @ValueSource(strings = {"C", "c", " c ", "c)", "opcion c", "Opci\u00f3n C", "respuesta c", "RESPUESTA C"})
    @DisplayName("acepta la letra correcta en distintos formatos")
    void aceptaLaLetraCorrecta(String respuesta) {
        Resultado r = responder(respuesta);

        assertEquals(1.0, r.getNota(), 1e-9);
        assertTrue(r.getResumen().contains("C) String"), r.getResumen());
    }

    @ParameterizedTest
    @ValueSource(strings = {"A", "b", "d", "opcion a"})
    @DisplayName("una letra valida pero equivocada puntua 0")
    void letraEquivocada(String respuesta) {
        Resultado r = responder(respuesta);

        assertEquals(0.0, r.getNota(), 1e-9);
        assertTrue(r.getResumen().startsWith("Respuesta incorrecta"), r.getResumen());
    }

    @Test
    @DisplayName("al fallar indica la letra elegida y la opcion correcta")
    void indicaLaLetraElegidaYLaCorrecta() {
        String resumen = responder("a").getResumen();

        assertTrue(resumen.contains("elegiste A"), resumen);
        assertTrue(resumen.contains("C) String"), resumen);
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "   ", "E", "ab", "c d", "string", "1", "opcion", "opcion e"})
    @DisplayName("una entrada que no es una letra valida puntua 0 y se marca como no valida")
    void entradaNoValida(String respuesta) {
        Resultado r = responder(respuesta);

        assertEquals(0.0, r.getNota(), 1e-9);
        assertTrue(r.getResumen().contains("no valida"), r.getResumen());
    }

    @Test
    @DisplayName("una respuesta null es no valida")
    void entradaNula() {
        Resultado r = responder(null);

        assertEquals(0.0, r.getNota(), 1e-9);
        assertTrue(r.getResumen().contains("no valida"), r.getResumen());
    }

    @Test
    @DisplayName("con tres opciones la letra D esta fuera de rango")
    void tresOpciones() {
        Pregunta tres = Pregunta.alternativas(4, "Cual?", 'B', "x", "y", "z");

        assertEquals(1.0, plugin.procesar("b", ctx(tres, 1)).getNota(), 1e-9);

        Resultado r = plugin.procesar("D", ctx(tres, 1));
        assertEquals(0.0, r.getNota(), 1e-9);
        assertTrue(r.getResumen().contains("A a C"), r.getResumen());
    }

    // ---------- Casos sin pregunta o con contexto no soportado ----------

    @Test
    @DisplayName("un contexto sin pregunta devuelve nota 0")
    void contextoSinPregunta() {
        Contexto sinPregunta = new Contexto(1, TipoPregunta.ALTERNATIVAS, null);

        Resultado r = plugin.procesar("C", sinPregunta);

        assertEquals(0.0, r.getNota(), 1e-9);
        assertTrue(r.getResumen().contains("Sin pregunta asociada"), r.getResumen());
    }

    @Test
    @DisplayName("un contexto de otro tipo devuelve nota 0 y lo indica")
    void contextoNoSoportado() {
        Pregunta corta = Pregunta.corta(5, "P?", "c");

        Resultado r = plugin.procesar("c", ctx(corta, 1));

        assertEquals(0.0, r.getNota(), 1e-9);
        assertTrue(r.getResumen().contains("no soporta"), r.getResumen());
    }
}

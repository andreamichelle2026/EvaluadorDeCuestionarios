package com.amcm.evaluadordecuestionarios.plugins;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.amcm.evaluadordecuestionarios.Concepto;
import com.amcm.evaluadordecuestionarios.Contexto;
import com.amcm.evaluadordecuestionarios.Pregunta;
import com.amcm.evaluadordecuestionarios.Resultado;
import com.amcm.evaluadordecuestionarios.SelectorPlugin;
import com.amcm.evaluadordecuestionarios.TipoPregunta;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class ProcesadorSimpleTest {

    private final ProcesadorSimple plugin = new ProcesadorSimple();

    private final Pregunta corta = Pregunta.corta(1, "Palabra clave de constante?", "final");
    private final Pregunta larga = Pregunta.larga(2, "Explica una clase.", new Concepto("plantilla", 1.0, "plantilla"));
    private final Pregunta alternativas = Pregunta.alternativas(3, "Cual es primitivo?", 'A', "int", "String");

    private static Contexto ctx(Pregunta pregunta, int letras) {
        return new Contexto(letras, pregunta.getTipo(), pregunta);
    }

    // ---------- Identidad y soporte ----------

    @Test
    @DisplayName("id es 'simple'")
    void idEsSimple() {
        assertEquals("simple", plugin.id());
    }

    @Test
    @DisplayName("soporta las preguntas cortas")
    void soportaCortas() {
        assertTrue(plugin.soporta(ctx(corta, 5)));
    }

    @Test
    @DisplayName("no soporta las preguntas de alternativas")
    void noSoportaAlternativas() {
        assertFalse(plugin.soporta(ctx(alternativas, 1)));
    }

    @Test
    @DisplayName("soporta las abiertas con menos letras que el minimo")
    void soportaAbiertasConPocoTexto() {
        assertTrue(plugin.soporta(ctx(larga, SelectorPlugin.TEXTO_MINIMO_ABIERTA - 1)));
    }

    @Test
    @DisplayName("no soporta las abiertas con texto suficiente")
    void noSoportaAbiertasConTextoSuficiente() {
        assertFalse(plugin.soporta(ctx(larga, SelectorPlugin.TEXTO_MINIMO_ABIERTA)));
    }

    // ---------- Evaluacion ----------

    @ParameterizedTest
    @ValueSource(strings = {"final", "FINAL", "  final  ", "final.", "Final!"})
    @DisplayName("acepta la respuesta correcta sin importar mayusculas, espacios ni signos")
    void aceptaLaRespuestaCorrecta(String respuesta) {
        Resultado r = plugin.procesar(respuesta, ctx(corta, respuesta.length()));

        assertEquals(1.0, r.getNota(), 1e-9);
        assertEquals("Respuesta correcta", r.getResumen());
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "   ", "finale", "static", "fin al"})
    @DisplayName("rechaza las respuestas incorrectas o vacias")
    void rechazaRespuestasIncorrectas(String respuesta) {
        Resultado r = plugin.procesar(respuesta, ctx(corta, respuesta.length()));

        assertEquals(0.0, r.getNota(), 1e-9);
        assertTrue(r.getResumen().startsWith("Respuesta incorrecta"), r.getResumen());
    }

    @Test
    @DisplayName("al fallar indica la respuesta esperada")
    void indicaLaRespuestaEsperada() {
        Resultado r = plugin.procesar("static", ctx(corta, 6));

        assertTrue(r.getResumen().contains("esperada: final"), r.getResumen());
    }

    @Test
    @DisplayName("acepta cualquiera de varias respuestas aceptadas")
    void variasRespuestasAceptadas() {
        Pregunta pregunta = Pregunta.corta(4, "Entero de 32 bits?", "int", "entero");

        assertEquals(1.0, plugin.procesar("ENTERO", ctx(pregunta, 6)).getNota(), 1e-9);
        assertEquals(1.0, plugin.procesar("int", ctx(pregunta, 3)).getNota(), 1e-9);
    }

    @Test
    @DisplayName("ignora las tildes al comparar")
    void ignoraTildes() {
        Pregunta pregunta = Pregunta.corta(5, "Como se llama?", "m\u00e9todo");

        assertEquals(1.0, plugin.procesar("metodo", ctx(pregunta, 6)).getNota(), 1e-9);
    }

    @Test
    @DisplayName("una respuesta null se trata como vacia")
    void entradaNula() {
        Resultado r = plugin.procesar(null, ctx(corta, 0));

        assertEquals(0.0, r.getNota(), 1e-9);
    }

    @Test
    @DisplayName("una abierta con poco texto se rechaza sin evaluar la rubrica")
    void abiertaConPocoTexto() {
        Resultado r = plugin.procesar("clase", ctx(larga, 5));

        assertEquals(0.0, r.getNota(), 1e-9);
        assertTrue(r.getResumen().contains("demasiado corta"), r.getResumen());
    }

    // ---------- Casos sin pregunta o con contexto no soportado ----------

    @Test
    @DisplayName("sin pregunta asociada cuenta las palabras")
    void sinPreguntaCuentaPalabras() {
        assertEquals("Palabras: 3", plugin.procesar("uno dos tres").getResumen());
        assertEquals("Palabras: 0", plugin.procesar("   ").getResumen());
        assertEquals("Palabras: 0", plugin.procesar(null).getResumen());
    }

    @Test
    @DisplayName("un contexto sin pregunta delega en el conteo de palabras")
    void contextoSinPregunta() {
        Contexto sinPregunta = new Contexto(9, TipoPregunta.CORTA, null);

        assertEquals("Palabras: 2", plugin.procesar("hola mundo", sinPregunta).getResumen());
    }

    @Test
    @DisplayName("un contexto no soportado devuelve nota 0 y lo indica")
    void contextoNoSoportado() {
        Resultado r = plugin.procesar("A", ctx(alternativas, 1));

        assertEquals(0.0, r.getNota(), 1e-9);
        assertTrue(r.getResumen().contains("no soporta"), r.getResumen());
    }
}

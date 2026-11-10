package com.amcm.evaluadordecuestionarios.plugins;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.amcm.evaluadordecuestionarios.BancoPreguntas;
import com.amcm.evaluadordecuestionarios.Concepto;
import com.amcm.evaluadordecuestionarios.Contexto;
import com.amcm.evaluadordecuestionarios.Pregunta;
import com.amcm.evaluadordecuestionarios.Resultado;
import com.amcm.evaluadordecuestionarios.TipoPregunta;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/** Prueba el plugin escrito en Scala desde Java (interoperabilidad JVM). */
class ProcesadorConceptosTest {

    private final ProcesadorConceptos plugin = new ProcesadorConceptos();

    /** Pesos: plantilla 1.0 + instancia 1.0 + atributos 0.5 = 2.5 */
    private final Pregunta rubrica = Pregunta.larga(1, "Explica clase y objeto.",
            new Concepto("plantilla", 1.0, "plantilla", "molde"),
            new Concepto("instancia", 1.0, "instancia", "instanci"),
            new Concepto("atributos", 0.5, "atributo", "estado"));

    private static Contexto ctx(Pregunta pregunta, String respuesta) {
        return new Contexto(respuesta.length(), pregunta.getTipo(), pregunta);
    }

    private Resultado evaluar(Pregunta pregunta, String respuesta) {
        return plugin.procesar(respuesta, ctx(pregunta, respuesta));
    }

    private static Pregunta bancoId(int id) {
        return BancoPreguntas.todas().stream()
                .filter(p -> p.getId() == id)
                .findFirst()
                .orElseThrow();
    }

    // ---------- Identidad y soporte ----------

    @Test
    @DisplayName("id es 'conceptos'")
    void idEsConceptos() {
        assertEquals("conceptos", plugin.id());
    }

    @Test
    @DisplayName("solo soporta las preguntas abiertas")
    void soloSoportaAbiertas() {
        Pregunta corta = Pregunta.corta(2, "P?", "x");
        Pregunta alternativas = Pregunta.alternativas(3, "P?", 'A', "a", "b");

        assertTrue(plugin.soporta(ctx(rubrica, "texto")));
        assertFalse(plugin.soporta(ctx(corta, "x")));
        assertFalse(plugin.soporta(ctx(alternativas, "a")));
    }

    // ---------- Nota por conceptos ----------

    @Test
    @DisplayName("todos los conceptos presentes -> nota 1.0")
    void todosLosConceptos() {
        Resultado r = evaluar(rubrica, "La clase es una plantilla y el objeto es una instancia con atributos");

        assertEquals(1.0, r.getNota(), 1e-9);
        assertTrue(r.getResumen().contains("3/3"), r.getResumen());
    }

    @Test
    @DisplayName("un concepto de peso 1.0 sobre 2.5 -> nota 0.4 y lista los que faltan")
    void unConcepto() {
        Resultado r = evaluar(rubrica, "es una plantilla");

        assertEquals(0.4, r.getNota(), 1e-9);
        assertTrue(r.getResumen().contains("1/3"), r.getResumen());
        assertTrue(r.getResumen().contains("Faltan: instancia, atributos"), r.getResumen());
    }

    @Test
    @DisplayName("el peso del concepto cuenta: 0.5 sobre 2.5 -> nota 0.2")
    void respetaElPeso() {
        assertEquals(0.2, evaluar(rubrica, "tiene atributos").getNota(), 1e-9);
    }

    @Test
    @DisplayName("ningun concepto -> nota 0 y 'ninguno'")
    void ningunConcepto() {
        Resultado r = evaluar(rubrica, "no se nada");

        assertEquals(0.0, r.getNota(), 1e-9);
        assertTrue(r.getResumen().contains("ninguno"), r.getResumen());
    }

    // ---------- Reglas de coincidencia ----------

    @Test
    @DisplayName("sinonimo largo vale como prefijo de palabra (instanciar cubre instanci)")
    void sinonimoLargoComoPrefijo() {
        assertEquals(0.4, evaluar(rubrica, "instanciar un objeto").getNota(), 1e-9);
    }

    @Test
    @DisplayName("sinonimo corto exige palabra exacta (for no coincide con formula)")
    void sinonimoCortoExigePalabraExacta() {
        Pregunta pregunta = Pregunta.larga(2, "Bucle for?", new Concepto("for", 1.0, "for"));

        assertEquals(0.0, evaluar(pregunta, "una formula").getNota(), 1e-9);
        assertEquals(1.0, evaluar(pregunta, "usa un for").getNota(), 1e-9);
    }

    @Test
    @DisplayName("sinonimo de varias palabras debe aparecer como frase completa")
    void sinonimoDeVariasPalabras() {
        Pregunta pregunta = Pregunta.larga(3, "Que cuenta?",
                new Concepto("iteraciones", 1.0, "numero de iteraciones"));

        assertEquals(1.0, evaluar(pregunta, "conoces el numero de iteraciones").getNota(), 1e-9);
        assertEquals(0.0, evaluar(pregunta, "numero de veces").getNota(), 1e-9);
    }

    @Test
    @DisplayName("ignora mayusculas, tildes y signos")
    void normalizaElTexto() {
        assertEquals(0.4, evaluar(rubrica, "PLANTILLA!!").getNota(), 1e-9);

        Pregunta pregunta = Pregunta.larga(4, "Que hay?", new Concepto("metodos", 1.0, "m\u00e9todo"));
        assertEquals(1.0, evaluar(pregunta, "Los M\u00c9TODOS").getNota(), 1e-9);
    }

    // ---------- Preguntas reales del banco ----------

    @Test
    @DisplayName("banco P9: una buena respuesta cubre los cuatro conceptos")
    void bancoPregunta9() {
        Resultado r = evaluar(bancoId(9),
                "Una clase es una plantilla y un objeto es una instancia con atributos y metodos");

        assertEquals(1.0, r.getNota(), 1e-9);
        assertTrue(r.getResumen().contains("4/4"), r.getResumen());
    }

    @Test
    @DisplayName("banco P10: solo el concepto 'for' (1.0 de 2.5) -> nota 0.4")
    void bancoPregunta10Parcial() {
        assertEquals(0.4, evaluar(bancoId(10), "for usa un contador").getNota(), 1e-9);
    }

    // ---------- Casos limite ----------

    @Test
    @DisplayName("una pregunta sin rubrica puntua 0 y lo indica")
    void preguntaSinRubrica() {
        Pregunta sinRubrica = Pregunta.larga(5, "Pregunta sin conceptos");

        Resultado r = evaluar(sinRubrica, "cualquier respuesta");

        assertEquals(0.0, r.getNota(), 1e-9);
        assertTrue(r.getResumen().contains("no tiene rubrica"), r.getResumen());
    }

    @Test
    @DisplayName("un contexto sin pregunta devuelve nota 0")
    void contextoSinPregunta() {
        Contexto sinPregunta = new Contexto(20, TipoPregunta.LARGA, null);

        Resultado r = plugin.procesar("plantilla e instancia", sinPregunta);

        assertEquals(0.0, r.getNota(), 1e-9);
        assertTrue(r.getResumen().contains("Sin pregunta asociada"), r.getResumen());
    }

    @Test
    @DisplayName("un contexto de otro tipo devuelve nota 0 y lo indica")
    void contextoNoSoportado() {
        Pregunta corta = Pregunta.corta(6, "P?", "x");

        Resultado r = plugin.procesar("x", ctx(corta, "x"));

        assertEquals(0.0, r.getNota(), 1e-9);
        assertTrue(r.getResumen().contains("no soporta"), r.getResumen());
    }
}

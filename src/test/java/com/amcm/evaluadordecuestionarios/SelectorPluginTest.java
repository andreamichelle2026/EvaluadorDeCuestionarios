package com.amcm.evaluadordecuestionarios;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class SelectorPluginTest {

    /** Plugin de prueba: su regla de soporte se inyecta como lambda. */
    private static final class PluginFalso implements ProcesadorPlugin {
        private final String id;
        private final Predicate<Contexto> regla;

        PluginFalso(String id, Predicate<Contexto> regla) {
            this.id = id;
            this.regla = regla;
        }

        @Override public String id() { return id; }
        @Override public boolean soporta(Contexto contexto) { return regla.test(contexto); }
        @Override public Resultado procesar(String entrada) { return new Resultado("falso", 0.0); }
    }

    private static final Pregunta CORTA = Pregunta.corta(1, "P?", "x");
    private static final Pregunta LARGA = Pregunta.larga(2, "P?", new Concepto("c", 1.0, "c"));
    private static final Pregunta ALTERNATIVAS = Pregunta.alternativas(3, "P?", 'A', "a", "b");

    private static Contexto ctx(Pregunta pregunta, int letras) {
        return new Contexto(letras, pregunta.getTipo(), pregunta);
    }

    // ---------- Reglas del selector con plugins de prueba ----------

    @Test
    @DisplayName("elige el primer plugin de la lista que soporta el contexto")
    void eligeElPrimeroQueSoporta() {
        ProcesadorPlugin a = new PluginFalso("a", c -> true);
        ProcesadorPlugin b = new PluginFalso("b", c -> true);

        Optional<ProcesadorPlugin> elegido = SelectorPlugin.seleccionar(ctx(CORTA, 3), List.of(a, b));

        assertTrue(elegido.isPresent());
        assertSame(a, elegido.get());
    }

    @Test
    @DisplayName("salta los plugins que no soportan el contexto")
    void saltaLosQueNoSoportan() {
        ProcesadorPlugin a = new PluginFalso("a", c -> false);
        ProcesadorPlugin b = new PluginFalso("b", c -> true);

        assertSame(b, SelectorPlugin.seleccionar(ctx(CORTA, 3), List.of(a, b)).get());
    }

    @Test
    @DisplayName("devuelve vacio si ningun plugin soporta el contexto")
    void ningunoSoporta() {
        ProcesadorPlugin a = new PluginFalso("a", c -> false);

        assertFalse(SelectorPlugin.seleccionar(ctx(CORTA, 3), List.of(a)).isPresent());
    }

    @Test
    @DisplayName("devuelve vacio con una lista de plugins vacia")
    void listaVacia() {
        assertFalse(SelectorPlugin.seleccionar(ctx(CORTA, 3), List.of()).isPresent());
    }

    @Test
    @DisplayName("la decision depende del contexto recibido")
    void dependeDelContexto() {
        ProcesadorPlugin soloCortas = new PluginFalso("cortas", c -> c.getTipo() == TipoPregunta.CORTA);

        assertTrue(SelectorPlugin.seleccionar(ctx(CORTA, 3), List.of(soloCortas)).isPresent());
        assertFalse(SelectorPlugin.seleccionar(ctx(LARGA, 3), List.of(soloCortas)).isPresent());
    }

    @Test
    @DisplayName("el motivo menciona tipo, letras, id del plugin y el archivo de configuracion")
    void motivoExplicaLaEleccion() {
        ProcesadorPlugin plugin = new PluginFalso("simple", c -> true);

        String motivo = SelectorPlugin.motivo(ctx(CORTA, 4), plugin);

        assertTrue(motivo.contains("CORTA"), motivo);
        assertTrue(motivo.contains("4"), motivo);
        assertTrue(motivo.contains("'simple'"), motivo);
        assertTrue(motivo.contains(CargadorPlugin.ARCHIVO), motivo);
    }

    // ---------- Integracion con los plugins reales descubiertos ----------

    private static String idElegido(Pregunta pregunta, int letras) {
        return SelectorPlugin.seleccionar(ctx(pregunta, letras), CargadorPlugin.descubrir())
                .map(ProcesadorPlugin::id)
                .orElse("ninguno");
    }

    @Test
    @DisplayName("real: pregunta corta -> simple")
    void realCorta() {
        assertEquals("simple", idElegido(CORTA, 5));
    }

    @Test
    @DisplayName("real: pregunta de alternativas -> alternativas")
    void realAlternativas() {
        assertEquals("alternativas", idElegido(ALTERNATIVAS, 1));
    }

    @Test
    @DisplayName("real: abierta con menos letras que el minimo -> simple")
    void realAbiertaCorta() {
        assertEquals("simple", idElegido(LARGA, SelectorPlugin.TEXTO_MINIMO_ABIERTA - 1));
    }

    @Test
    @DisplayName("real: abierta con el minimo de letras -> conceptos")
    void realAbiertaEnElLimite() {
        assertEquals("conceptos", idElegido(LARGA, SelectorPlugin.TEXTO_MINIMO_ABIERTA));
    }

    @Test
    @DisplayName("real: abierta con mucho texto -> conceptos")
    void realAbiertaLarga() {
        assertEquals("conceptos", idElegido(LARGA, 200));
    }
}

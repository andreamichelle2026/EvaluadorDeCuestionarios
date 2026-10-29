package com.amcm.evaluadordecuestionarios;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class CargadorPluginTest {

    @Test
    void cargaPorReflexionElPluginJava() throws Exception {
        String nombre = SelectorPlugin.seleccionar(new Contexto(5, true));
        ProcesadorPlugin plugin = CargadorPlugin.crear(nombre);
        assertEquals("simple", plugin.id());
    }

    @Test
    void cargaPorReflexionElPluginScala() throws Exception {
        String nombre = SelectorPlugin.seleccionar(new Contexto(80, false));
        ProcesadorPlugin plugin = CargadorPlugin.crear(nombre);
        assertEquals("conceptos", plugin.id());
    }

    @Test
    void rechazaUnaClaseQueNoCumpleElContrato() {
        assertThrows(IllegalArgumentException.class,
                () -> CargadorPlugin.crear("java.lang.String"));
    }

    @Test
    void fallaSiLaClaseNoExiste() {
        assertThrows(ClassNotFoundException.class,
                () -> CargadorPlugin.crear("no.existe.PluginFalso"));
    }
}

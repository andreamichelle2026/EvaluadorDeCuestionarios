package com.amcm.evaluadordecuestionarios;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CargadorPluginTest {

    private static final String PAQUETE = "com.amcm.evaluadordecuestionarios.plugins.";

    @Test
    @DisplayName("crear instancia un plugin valido por su nombre de clase")
    void creaUnPluginValido() throws Exception {
        ProcesadorPlugin plugin = CargadorPlugin.crear(PAQUETE + "ProcesadorSimple");

        assertEquals("simple", plugin.id());
    }

    @Test
    @DisplayName("crear falla si la clase no existe")
    void claseInexistente() {
        assertThrows(ClassNotFoundException.class, () -> CargadorPlugin.crear("no.existe.Plugin"));
    }

    @Test
    @DisplayName("crear rechaza una clase que no cumple el contrato ProcesadorPlugin")
    void claseSinContrato() {
        assertThrows(IllegalArgumentException.class, () -> CargadorPlugin.crear("java.lang.String"));
    }

    @Test
    @DisplayName("crear no puede instanciar una clase abstracta")
    void claseAbstracta() {
        assertThrows(ReflectiveOperationException.class,
                () -> CargadorPlugin.crear("com.amcm.evaluadordecuestionarios.PluginBase"));
    }

    @Test
    @DisplayName("leerNombres devuelve las clases de plugins.properties en su orden")
    void leeLosNombresEnOrden() throws Exception {
        List<String> nombres = CargadorPlugin.leerNombres();

        assertEquals(3, nombres.size());
        assertEquals(PAQUETE + "ProcesadorAlternativas", nombres.get(0));
        assertEquals(PAQUETE + "ProcesadorSimple", nombres.get(1));
        assertEquals(PAQUETE + "ProcesadorConceptos", nombres.get(2));
    }

    @Test
    @DisplayName("descubrir construye los tres plugins en el orden de prioridad")
    void descubreLosTresPlugins() {
        List<ProcesadorPlugin> plugins = CargadorPlugin.descubrir();

        assertEquals(3, plugins.size());
        assertEquals("alternativas", plugins.get(0).id());
        assertEquals("simple", plugins.get(1).id());
        assertEquals("conceptos", plugins.get(2).id());
    }

    @Test
    @DisplayName("descubrir devuelve una lista que no se puede modificar")
    void listaInmutable() {
        List<ProcesadorPlugin> plugins = CargadorPlugin.descubrir();

        assertTrue(plugins.size() > 0);
        assertThrows(UnsupportedOperationException.class, () -> plugins.add(plugins.get(0)));
    }
}

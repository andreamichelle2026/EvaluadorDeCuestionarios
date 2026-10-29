package com.amcm.evaluadordecuestionarios;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class SelectorPluginTest {

    @Test
    void preguntaCortaSeleccionaProcesadorSimple() {
        Contexto contexto = new Contexto(5, true);
        assertTrue(SelectorPlugin.seleccionar(contexto).endsWith(".ProcesadorSimple"));
    }

    @Test
    void preguntaAbiertaSeleccionaProcesadorConceptos() {
        Contexto contexto = new Contexto(80, false);
        assertTrue(SelectorPlugin.seleccionar(contexto).endsWith(".ProcesadorConceptos"));
    }

    @Test
    void elMotivoNoEstaVacio() {
        assertFalse(SelectorPlugin.motivo(new Contexto(5, true)).isBlank());
        assertFalse(SelectorPlugin.motivo(new Contexto(80, false)).isBlank());
    }
}

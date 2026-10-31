package com.amcm.evaluadordecuestionarios;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class TextoUtilTest {

    @Test
    void normalizaMayusculasTildesYSignos() {
        assertEquals("metodo final", TextoUtil.normalizar("  Método, FINAL! "));
    }

    @Test
    void nuloSeConvierteEnTextoVacio() {
        assertEquals("", TextoUtil.normalizar(null));
    }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.amcm.evaluadordecuestionarios;

/**
 * Clase que decide qué plugin evalúa cada pregunta. 
 * No ejecuta nada ni crea objetos: 
 * recibe un Contexto y devuelve el nombre de la clase del plugin elegido, 
 * más un texto que explica por qué.
 *
 *  - pregunta de alternativas                              -> ProcesadorAlternativas (Java)
 *  - modoValidacion = true  (respuesta corta)              -> ProcesadorSimple       (Java)
 *  - modoValidacion = false (respuesta larga -10 letras)   -> ProcesadorSimple (Java)   
 *  - modoValidacion = false (respuesta larga)              -> ProcesadorConceptos (Scala)
 */
public final class SelectorPlugin {

    public static final String CLASE_SIMPLE = "com.amcm.evaluadordecuestionarios.plugins.ProcesadorSimple";
    public static final String CLASE_ALTERNATIVAS = "com.amcm.evaluadordecuestionarios.plugins.ProcesadorAlternativas";
    public static final String CLASE_CONCEPTOS = "com.amcm.evaluadordecuestionarios.plugins.ProcesadorConceptos";
    public static final int TEXTO_MINIMO_ABIERTA = 10;   // caracteres

    private SelectorPlugin() {}

    public static String seleccionar(Contexto contexto) {
        if (contexto.isAlternativas()) {
            return CLASE_ALTERNATIVAS;
        }
        if (contexto.isModoValidacion()) {
            return CLASE_SIMPLE;
        }
        // pregunta abierta: el tamano decide
        return contexto.getTamanioTexto() < TEXTO_MINIMO_ABIERTA ? CLASE_SIMPLE : CLASE_CONCEPTOS;
    }

    /** Explica por que la regla eligio esa clase (se muestra al usuario). */
    public static String motivo(Contexto contexto) {
        String tamanio = " (respuesta de " + contexto.getTamanioTexto() + " caracteres)";
        if (contexto.isAlternativas()) {
            return "pregunta de alternativas -> se compara la letra elegida con la opcion correcta" + tamanio;
        }
        if (contexto.isModoValidacion()) {
            return "pregunta de respuesta corta -> modo validacion: se compara con las respuestas aceptadas" + tamanio;
        }
        if (contexto.getTamanioTexto() < TEXTO_MINIMO_ABIERTA) {
            return "pregunta abierta con respuesta menor a " + TEXTO_MINIMO_ABIERTA
                + " caracteres -> no hay texto suficiente para la rubrica, la valida el plugin simple" + tamanio;
        }
        return "pregunta abierta con texto suficiente (>= " + TEXTO_MINIMO_ABIERTA
            + " caracteres) -> se evaluan los conceptos de la rubrica" + tamanio;
}
}


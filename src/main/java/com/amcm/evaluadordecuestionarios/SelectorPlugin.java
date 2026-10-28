/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.amcm.evaluadordecuestionarios;

/**
 * Regla de seleccion: devuelve el NOMBRE COMPLETO de la clase del plugin a usar.
 *
 *  - modoValidacion = true  (pregunta de respuesta corta) -> ProcesadorSimple   (Java)
 *  - modoValidacion = false (pregunta abierta)            -> ProcesadorConceptos (Scala)
 */
public final class SelectorPlugin {

    public static final String CLASE_SIMPLE = "com.acme.quiz.plugins.ProcesadorSimple";
    public static final String CLASE_CONCEPTOS = "com.acme.quiz.plugins.ProcesadorConceptos";

    private SelectorPlugin() {}

    public static String seleccionar(Contexto contexto) {
        return contexto.isModoValidacion() ? CLASE_SIMPLE : CLASE_CONCEPTOS;
    }

    /** Explica por que la regla eligio esa clase (se muestra al usuario). */
    public static String motivo(Contexto contexto) {
        String tamanio = " (respuesta de " + contexto.getTamanioTexto() + " caracteres)";
        if (contexto.isModoValidacion()) {
            return "pregunta de respuesta corta -> modo validacion: se compara con las respuestas aceptadas" + tamanio;
        }
        return "pregunta abierta -> sin modo validacion: se evaluan los conceptos de la rubrica" + tamanio;
    }
}

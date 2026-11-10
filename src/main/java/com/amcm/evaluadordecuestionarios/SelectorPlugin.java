/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.amcm.evaluadordecuestionarios;

import java.util.List;
import java.util.Optional;

/**
 * Regla de seleccion: de los plugins descubiertos (en el orden de plugins.properties)
 * elige el PRIMERO cuyo metodo soporta(contexto) devuelva true.
 *
 * Las reglas concretas viven en cada plugin:
 *  - por tipo de pregunta: alternativas / corta / abierta;
 *  - por tamanio de la respuesta: una abierta con menos de TEXTO_MINIMO_ABIERTA
 *    letras la atiende el plugin simple, no el de conceptos.
 */
public final class SelectorPlugin {

    /** Letras minimas para que una pregunta abierta se evalue por conceptos. */
    public static final int TEXTO_MINIMO_ABIERTA = 10;

    private SelectorPlugin() {}

    public static Optional<ProcesadorPlugin> seleccionar(Contexto contexto, List<ProcesadorPlugin> disponibles) {
        return disponibles.stream()
                .filter(plugin -> plugin.soporta(contexto))
                .findFirst();
    }

    /** Explica por que se eligio ese plugin (se muestra al usuario). */
    public static String motivo(Contexto contexto, ProcesadorPlugin elegido) {
        return "pregunta " + contexto.getTipo() + ", " + contexto.getTamanioTexto()
                + " letras -> '" + elegido.id() + "' es el primer plugin de "
                + CargadorPlugin.ARCHIVO + " que soporta este contexto";
    }
}

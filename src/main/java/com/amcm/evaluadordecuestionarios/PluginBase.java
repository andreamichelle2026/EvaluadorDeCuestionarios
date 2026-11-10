/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.amcm.evaluadordecuestionarios;

import java.util.Objects;

/**
 * Clase base abstracta de los plugins (patron Metodo Plantilla).
 *
 * El esqueleto de procesar(entrada, contexto) es comun y NO se puede sobrescribir:
 *  1) si no hay pregunta asociada, delega en procesar(String);
 *  2) si el plugin no soporta el contexto (tipo de pregunta), devuelve nota 0;
 *  3) si todo es valido, delega en el metodo abstracto evaluar(...), que es lo
 *     unico que cada plugin concreto debe implementar.
 *
 * Cada subclase debe tener un constructor publico sin argumentos para que
 * CargadorPlugin pueda instanciarla por reflexion.
 */
public abstract class PluginBase implements ProcesadorPlugin {

    private final String id;

    protected PluginBase(String id) {
        this.id = Objects.requireNonNull(id, "id del plugin");
    }

    @Override
    public final String id() {
        return id;
    }

    /** Comportamiento por defecto cuando no hay pregunta; las subclases pueden sobrescribirlo. */
    @Override
    public Resultado procesar(String entrada) {
        return new Resultado("Sin pregunta asociada: el plugin '" + id + "' no puede evaluar la respuesta", 0.0);
    }

    /** Metodo plantilla: valida y delega en evaluar(...). */
    @Override
    public final Resultado procesar(String entrada, Contexto contexto) {
        if (contexto == null || contexto.getPregunta() == null) {
            return procesar(entrada);
        }
        if (!soporta(contexto)) {
            return new Resultado("El plugin '" + id + "' no soporta preguntas de tipo "
                    + contexto.getTipo(), 0.0);
        }
        String texto = (entrada == null) ? "" : entrada;
        return evaluar(texto, contexto.getPregunta(), contexto);
    }

    /** Paso variable: la logica de evaluacion propia de cada plugin. */
    protected abstract Resultado evaluar(String entrada, Pregunta pregunta, Contexto contexto);
}

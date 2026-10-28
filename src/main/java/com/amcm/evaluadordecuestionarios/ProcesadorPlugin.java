/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.amcm.evaluadordecuestionarios;

/** Contrato comun de los plugins. */
public interface ProcesadorPlugin {
    String id();
    boolean soporta(Contexto contexto);
    Resultado procesar(String entrada);

    /**
     * Recibe tambien el Contexto, para que el plugin pueda leer la pregunta
     * que esta evaluando. 
     * Por defecto delega en procesar(String), asi que cualquier plugin
     * que solo implemente el contrato original sigue siendo valido.
    */
    default Resultado procesar(String entrada, Contexto contexto) {
        return procesar(entrada);
    }

}


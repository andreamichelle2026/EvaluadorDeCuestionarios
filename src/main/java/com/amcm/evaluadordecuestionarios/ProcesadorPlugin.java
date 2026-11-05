/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.amcm.evaluadordecuestionarios;

/*
 * ProcesadorPlugin es una interfaz: 
 * Define el CONTRATO que debe cumplir toda clase para ser un plugin. 
 * No contiene lógica de evaluación, solo dice qué métodos debe tener un plugin. 
 */
public interface ProcesadorPlugin {
    String id();
    boolean soporta(Contexto contexto);
    Resultado procesar(String entrada);

    /**
     * Recibe tambien el Contexto, para que el plugin pueda leer la pregunta
     * que esta evaluando. 
    */
    default Resultado procesar(String entrada, Contexto contexto) {
        return procesar(entrada);
    }

}


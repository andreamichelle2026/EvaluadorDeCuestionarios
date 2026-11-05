/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.amcm.evaluadordecuestionarios;

/* 
 * Crea un plugin a partir del nombre de clase en texto:
 *  CLASE_SIMPLE, 
 *  CLASE_ALTERNATIVAS, 
 *  CLASE_CONCEPTOS
 * sin que el código conozca esa clase al compilar. 
 * Usa reflexión de Java para esto.
 */

public class CargadorPlugin {

    public static ProcesadorPlugin crear(String nombreClase) throws Exception {
        
        // Carga en memoria y devuelve un objeto Class. Si no existe lanza ClassNotFoundException.
        Class<?> clase = Class.forName(nombreClase); 

        // Comprueba que la clase cargada implemente la interfaz ProcesadorPlugin
        if (!ProcesadorPlugin.class.isAssignableFrom(clase)) {
            throw new IllegalArgumentException("La clase no cumple el contrato ProcesadorPlugin");
        }

        // Obtiene el constructor sin argumentos y crea una instancia, en tiempo de ejecución
        Object objeto = clase.getDeclaredConstructor().newInstance();
        return (ProcesadorPlugin) objeto;
    }
}
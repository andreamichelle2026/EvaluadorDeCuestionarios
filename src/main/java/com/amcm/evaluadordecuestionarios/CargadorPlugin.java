/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.amcm.evaluadordecuestionarios;

public class CargadorPlugin {

    public static ProcesadorPlugin crear(String nombreClase) throws Exception {
        Class<?> clase = Class.forName(nombreClase);

        if (!ProcesadorPlugin.class.isAssignableFrom(clase)) {
            throw new IllegalArgumentException("La clase no cumple el contrato ProcesadorPlugin");
        }

        Object objeto = clase.getDeclaredConstructor().newInstance();
        return (ProcesadorPlugin) objeto;
    }
}
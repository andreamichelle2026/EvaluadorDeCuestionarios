/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.amcm.evaluadordecuestionarios;

/** Registro de una ejecucion: que plugin se uso para cada pregunta y con que resultado. */
public record RegistroEjecucion(
        int idPregunta,
        TipoPregunta tipo,
        String idPlugin,
        String nombreClase,
        double nota,
        String estado) {
}

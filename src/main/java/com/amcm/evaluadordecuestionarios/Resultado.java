/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.amcm.evaluadordecuestionarios;

/*
 * Clase de datos que representa lo que devuelve un plugin después de evaluar una respuesta. 
 * Agrupa dos cosas en un solo objeto: un mensaje y una nota.
*/

public class Resultado {
    private String resumen;
    private double nota;   // extension: nota entre 0.0 y 1.0

    public Resultado(String resumen, double nota) {
        this.resumen = resumen;
        this.nota = nota;
    }

    public String getResumen() { return resumen; }
    public double getNota() { return nota; }
}

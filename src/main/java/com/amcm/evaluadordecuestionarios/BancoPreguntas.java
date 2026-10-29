/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.amcm.evaluadordecuestionarios;

import java.util.List;

/** 10 preguntas de fundamentos de programacion en Java: 6 cortas + 4 abiertas. */
public final class BancoPreguntas {

    private BancoPreguntas() {}

    public static List<Pregunta> todas() {
        return List.of(
            // ---------- Respuesta corta (modo validacion -> plugin Java) ----------
            Pregunta.corta(1, "Que palabra clave se usa para declarar una constante (variable que no cambia)?", "final"),
            Pregunta.corta(2, "Cual es el tipo primitivo para numeros enteros de 32 bits?", "int"),
            Pregunta.corta(3, "Que palabra clave se usa para crear un objeto nuevo?", "new"),
            Pregunta.corta(4, "Como se llama el metodo donde empieza a ejecutarse un programa Java?", "main"),
            Pregunta.corta(5, "Que palabra clave se usa para que una clase herede de otra?", "extends"),
            Pregunta.corta(6, "Que tipo primitivo almacena solo verdadero o falso?", "boolean"),

            // ---------- Respuesta abierta (sin modo validacion -> plugin Scala) ----------
            Pregunta.larga(7, "Explica que es una clase y que es un objeto en Java.",
                new Concepto("clase como plantilla", 1.0, "plantilla", "molde", "modelo", "plano", "blueprint"),
                new Concepto("objeto como instancia", 1.0, "instancia", "instanci", "ejemplar"),
                new Concepto("atributos / estado", 1.0, "atributo", "propiedad", "campo", "estado", "variables"),
                new Concepto("metodos / comportamiento", 1.0, "metodo", "comportamiento", "acciones", "funciones")),
            Pregunta.larga(8, "Explica la diferencia entre un bucle for y un bucle while.",
                new Concepto("for: iteraciones conocidas", 1.0, "for", "contador",
                        "numero de iteraciones", "numero de repeticiones", "cantidad de repeticiones", "veces conocidas"),
                new Concepto("while: depende de una condicion", 1.0, "while", "condicion", "mientras", "se cumpla"),
                new Concepto("ambos repiten codigo", 0.5, "repit", "repeti", "iterac", "itera", "ciclo")),
            Pregunta.larga(9, "Que es la herencia y para que sirve?",
                new Concepto("subclase (hija)", 1.0, "subclase", "clase hija", "hija", "derivada", "clase derivada"),
                new Concepto("superclase (padre)", 1.0, "superclase", "clase padre", "padre", "clase base"),
                new Concepto("reutilizacion de codigo", 1.0, "reutiliz", "reutilic", "reutilizar", "reusar", "hered", "aprovech")),
            Pregunta.larga(10, "Que son las excepciones y como se usa try-catch?",
                new Concepto("excepcion = error en ejecucion", 1.0, "excepcion", "error", "situacion anomala", "falla"),
                new Concepto("try: codigo que puede fallar", 1.0, "try", "puede fallar", "podria fallar", "codigo riesgoso", "intentar"),
                new Concepto("catch: captura / maneja el error", 1.0, "catch", "captur", "atrap", "manej"),
                new Concepto("finally: se ejecuta siempre", 0.5, "finally", "siempre se ejecuta", "liberar recursos", "limpieza"))
        );
    }
}
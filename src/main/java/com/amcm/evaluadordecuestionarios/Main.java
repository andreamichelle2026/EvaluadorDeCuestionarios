/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.amcm.evaluadordecuestionarios;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Cuestionario de fundamentos de Java
 *
 * Por cada pregunta:
 *  1) se construye el Contexto (tamanio de la respuesta, modo validacion, pregunta);
 *  2) SelectorPlugin devuelve el nombre completo de la clase a usar y el motivo;
 *  3) CargadorPlugin la obtiene con Class.forName y valida que cumpla ProcesadorPlugin;
 *  4) se ejecuta el metodo comun procesar(...) y se muestra que plugin se uso y por que.
 */
public class Main {

    public static void main(String[] args) {
        List<Pregunta> preguntas = BancoPreguntas.todas();
        List<RegistroEjecucion> registros = new ArrayList<>();
        Scanner teclado = new Scanner(System.in);

        System.out.println("=== Cuestionario: fundamentos de Java (plugins Java + Scala) ===");
        System.out.println("Responde cada pregunta y pulsa Enter. Son " + preguntas.size() + " preguntas.");

        for (Pregunta pregunta : preguntas) {
            System.out.printf("%nPregunta %d/%d [%s]%n%s%n> ",
                    pregunta.getId(), preguntas.size(), pregunta.getTipo(), pregunta.getEnunciado());
            String entrada = teclado.hasNextLine() ? teclado.nextLine() : "";

            // 1) Contexto
            boolean modoValidacion = pregunta.getTipo() == TipoPregunta.CORTA;
            Contexto contexto = new Contexto(entrada.length(), modoValidacion, pregunta);

            // 2) Regla de seleccion: nombre completo de la clase + motivo
            String nombreClase = SelectorPlugin.seleccionar(contexto);
            String motivo = SelectorPlugin.motivo(contexto);

            try {
                // 3) Reflection + validacion del contrato
                ProcesadorPlugin plugin = CargadorPlugin.crear(nombreClase);
                if (!plugin.soporta(contexto)) {
                    throw new IllegalStateException("el plugin '" + plugin.id() + "' no soporta este contexto");
                }

                // 4) Ejecutar el metodo comun y registrar
                Resultado resultado = plugin.procesar(entrada, contexto);

                System.out.println();
                System.out.println("  Plugin usado : " + plugin.id());
                System.out.println("  Clase cargada: " + nombreClase);
                System.out.println("  Contrato     : cumple ProcesadorPlugin (validado)");
                System.out.println("  Motivo       : " + motivo);
                System.out.println("  Resultado    : " + resultado.getResumen());
                System.out.printf("  Nota         : %.2f%n", resultado.getNota());

                registros.add(new RegistroEjecucion(pregunta.getId(), pregunta.getTipo(),
                        plugin.id(), nombreClase, resultado.getNota(), "OK"));
            } catch (Exception e) {
                System.out.println();
                System.out.println("  No se pudo ejecutar el plugin " + nombreClase + ": " + e.getMessage());
                registros.add(new RegistroEjecucion(pregunta.getId(), pregunta.getTipo(),
                        "-", nombreClase, 0.0, "ERROR"));
            }
        }

        imprimirRegistro(registros);
    }

    private static void imprimirRegistro(List<RegistroEjecucion> registros) {
        System.out.println();
        System.out.println("=== Registro de ejecucion ===");
        System.out.printf("%-4s %-8s %-10s %-22s %-7s %5s%n", "P", "Tipo", "Plugin", "Clase", "Estado", "Nota");
        System.out.println("-".repeat(62));

        double total = 0.0;
        for (RegistroEjecucion r : registros) {
            total += r.nota();
            String clase = r.nombreClase().substring(r.nombreClase().lastIndexOf('.') + 1);
            System.out.printf("P%-3d %-8s %-10s %-22s %-7s %5.2f%n",
                    r.idPregunta(), r.tipo(), r.idPlugin(), clase, r.estado(), r.nota());
        }
        System.out.println("-".repeat(62));
        System.out.printf("Nota final: %.1f / %d%n", total, registros.size());
    }
}


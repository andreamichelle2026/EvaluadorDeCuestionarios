/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.amcm.evaluadordecuestionarios;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

/**
 * Cuestionario de fundamentos de Java.
 *
 * Al inicio: CargadorPlugin descubre los plugins listados en plugins.properties.
 * Por cada pregunta:
 *  1) MonitorTexto observa la respuesta y la pregunta y construye el Contexto;
 *  2) SelectorPlugin elige el primer plugin descubierto que soporte el contexto;
 *  3) se ejecuta el metodo comun procesar(...) y se muestra que plugin se uso y por que;
 *  4) se guarda un RegistroEjecucion.
 */
public class Main {

    public static void main(String[] args) {
        List<Pregunta> preguntas = BancoPreguntas.todas();
        List<RegistroEjecucion> registros = new ArrayList<>();
        Scanner teclado = new Scanner(System.in);

        List<ProcesadorPlugin> plugins = CargadorPlugin.descubrir();

        System.out.println("=== Cuestionario: fundamentos de Java (plugins Java + Scala) ===");
        System.out.println("Plugins descubiertos en " + CargadorPlugin.ARCHIVO + " (" + plugins.size() + "):");
        plugins.forEach(p -> System.out.println("  - " + p.id() + " -> " + p.getClass().getName()));
        System.out.println("Responde cada pregunta y pulsa Enter. Son " + preguntas.size() + " preguntas.");

        for (Pregunta pregunta : preguntas) {
            System.out.printf("%nPregunta %d/%d [%s]%n%s%n",
                    pregunta.getId(), pregunta.getTipo(), pregunta.getEnunciado());
            if (pregunta.getTipo() == TipoPregunta.ALTERNATIVAS) {
                char letra = 'A';
                for (String opcion : pregunta.getOpciones()) {
                    System.out.println("  " + letra + ") " + opcion);
                    letra++;
                }
            }
            System.out.print("> ");
            String entrada = teclado.hasNextLine() ? teclado.nextLine() : "";

            // 1) Monitor: observa y construye el Contexto
            Contexto contexto = MonitorTexto.observar(entrada, pregunta);

            // 2) Seleccion entre los plugins descubiertos
            Optional<ProcesadorPlugin> elegido = SelectorPlugin.seleccionar(contexto, plugins);
            if (elegido.isEmpty()) {
                System.out.println();
                System.out.println("  Ningun plugin descubierto soporta este contexto (tipo "
                        + contexto.getTipo() + ")");
                registros.add(new RegistroEjecucion(pregunta.getId(), pregunta.getTipo(),
                        "-", "-", 0.0, "ERROR"));
                continue;
            }

            ProcesadorPlugin plugin = elegido.get();
            String nombreClase = plugin.getClass().getName();

            try {
                // 3) Ejecutar el metodo comun
                Resultado resultado = plugin.procesar(entrada, contexto);

                System.out.println();
                System.out.println("  Plugin usado : " + plugin.id());
                System.out.println("  Clase        : " + nombreClase);
                System.out.println("  Contrato     : ProcesadorPlugin (validado al descubrirlo)");
                System.out.println("  Motivo       : " + SelectorPlugin.motivo(contexto, plugin));
                System.out.println("  Resultado    : " + resultado.getResumen());
                System.out.printf("  Nota         : %.2f%n", resultado.getNota());

                // 4) Registrar
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
        final String formato = "%-4s %-12s %-12s %-22s %-7s %5s%n";
        System.out.println();
        System.out.println("=== Registro de ejecucion ===");
        System.out.printf(formato, "P", "Tipo", "Plugin", "Clase", "Estado", "Nota");
        System.out.println("-".repeat(67));

        for (RegistroEjecucion r : registros) {
            String clase = r.nombreClase().substring(r.nombreClase().lastIndexOf('.') + 1);
            System.out.printf("P%-3d %-12s %-12s %-22s %-7s %5.2f%n",
                    r.idPregunta(), r.tipo(), r.idPlugin(), clase, r.estado(), r.nota());
        }
        System.out.println("-".repeat(67));

        double total = registros.stream().mapToDouble(RegistroEjecucion::nota).sum();
        System.out.printf("Nota final: %.1f / %d  (equivalente a %.1f / 10)%n",
                total, registros.size(), registros.isEmpty() ? 0.0 : total * 10.0 / registros.size());
    }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.amcm.evaluadordecuestionarios;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

/**
 * Descubre, valida y construye plugins por reflexion.
 *
 *  - descubrir(): lee plugins.properties del classpath y, por cada clase listada,
 *    la carga (Class.forName), valida el contrato y la instancia.
 *  - crear(nombre): carga, valida e instancia una sola clase.
 */
public final class CargadorPlugin {

    /** Archivo de configuracion (en src/main/resources). */
    public static final String ARCHIVO = "plugins.properties";
    /** Clave que contiene la lista de clases separadas por coma. */
    public static final String CLAVE = "plugins";

    private CargadorPlugin() {}

    /** Carga, valida el contrato e instancia una clase de plugin. */
    public static ProcesadorPlugin crear(String nombreClase) throws Exception {
        Class<?> clase = Class.forName(nombreClase);

        if (!ProcesadorPlugin.class.isAssignableFrom(clase)) {
            throw new IllegalArgumentException("La clase no cumple el contrato ProcesadorPlugin");
        }

        Object objeto = clase.getDeclaredConstructor().newInstance();
        return (ProcesadorPlugin) objeto;
    }

    /** Lee de plugins.properties los nombres de clase, en el orden escrito. */
    public static List<String> leerNombres() throws IOException {
        Properties props = new Properties();
        try (InputStream in = CargadorPlugin.class.getClassLoader().getResourceAsStream(ARCHIVO)) {
            if (in == null) {
                throw new FileNotFoundException("no se encontro " + ARCHIVO + " en el classpath");
            }
            props.load(new InputStreamReader(in, StandardCharsets.UTF_8));
        }
        String valor = props.getProperty(CLAVE, "");
        return Arrays.stream(valor.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .toList();
    }

    /**
     * Descubre todos los plugins validos listados en plugins.properties.
     * Los que fallan (clase inexistente, contrato incumplido, sin constructor
     * vacio, id repetido...) se ignoran con un aviso, sin detener el programa.
     */
    public static List<ProcesadorPlugin> descubrir() {
        List<String> nombres;
        try {
            nombres = leerNombres();
        } catch (IOException e) {
            System.err.println("[aviso] no se pudo leer " + ARCHIVO + ": " + e.getMessage());
            return List.of();
        }

        List<ProcesadorPlugin> plugins = new ArrayList<>();
        Set<String> idsVistos = new HashSet<>();

        for (String nombre : nombres) {
            try {
                ProcesadorPlugin plugin = crear(nombre);
                if (!idsVistos.add(plugin.id())) {
                    System.err.println("[aviso] plugin ignorado " + nombre
                            + ": id repetido '" + plugin.id() + "'");
                    continue;
                }
                plugins.add(plugin);
            } catch (Exception | LinkageError e) {
                System.err.println("[aviso] plugin ignorado " + nombre + ": " + e);
            }
        }
        return List.copyOf(plugins);
    }
}

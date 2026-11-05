/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.amcm.evaluadordecuestionarios;

import java.text.Normalizer;
import java.util.Locale;

/*
 * Normalizacion de texto compartida por los plugins (Java y Scala). 
 */
public final class TextoUtil {

    private TextoUtil() {}

    /** Minusculas, sin tildes ni signos de puntuacion, con espacios simples. */
    public static String normalizar(String s) {
        if (s == null) return "";
        String d = Normalizer.normalize(s, Normalizer.Form.NFD).replaceAll("\\p{M}", "");
        return d.toLowerCase(Locale.ROOT).replaceAll("[^a-z0-9]+", " ").trim();
    }
}
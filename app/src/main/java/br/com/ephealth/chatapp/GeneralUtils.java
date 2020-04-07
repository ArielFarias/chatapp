package br.com.ephealth.chatapp;

import java.text.Normalizer;

public abstract class GeneralUtils {

    public static String normalizedName(String s) {
        s = Normalizer.normalize(s, Normalizer.Form.NFD);
        return s.toUpperCase();
    }
}

package com.example.organizze.helper;

import android.util.Base64;

public class Base64Custom {
    public static String codificaBase64(String texto){
        return Base64.encodeToString(texto.getBytes(),Base64.DEFAULT).replaceAll("(\\n|\\r)","");
    }

    public static String decodificaBase64(String texto){
        return new String(Base64.decode(texto,Base64.DEFAULT));
    }
}

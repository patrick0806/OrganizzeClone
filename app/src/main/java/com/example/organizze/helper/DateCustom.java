package com.example.organizze.helper;

import java.text.SimpleDateFormat;

public class DateCustom {
    public static String dataAtual(){
        long data = System.currentTimeMillis();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String dataString = simpleDateFormat.format(data);
        return dataString;
    }

    public static String mesAnoDataEscolhida(String data){
        String retornData[] = data.split("/");
        String dia = retornData[0];
        String mes = retornData[1];
        String ano = retornData[2];

        return mes+ano;
    }
}

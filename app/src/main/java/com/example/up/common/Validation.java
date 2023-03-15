package com.example.up.common;
import android.util.Log;
public class Validation {
    public static boolean mailValidation(String text) {
        if(text.contains("@")) {
            String[] value = text.split("@");
            if(value.length == 2) {
                if(symbolValidation(value[0], true)) {
                    if(value[1].contains(".")) {
                        String[] valueDomen = value[1].split("\\.");
                        if(valueDomen.length == 2) {
                            if(symbolValidation(valueDomen[0], true)) {
                                if(symbolValidation(valueDomen[1], false)) {
                                    if(valueDomen[1].length() < 4) {
                                        return true;
                                    } else Log.e("MV", "Верхний регист более трёх символов.");
                                } else Log.e("MV", "Символы используемые в домене верхнего уровня не являются символами нижнего регистра.");
                            } else Log.e("MV", "Символы используемые в домене второго уровня не являются символами нижнего регистра или цифрами.");
                        } else Log.e("MV", "Домен верхнего уровня отсутствуют.");
                    } else Log.e("MV", "Входная строка не содержит символ .");
                } else Log.e("MV", "Символы используемые в имени не являются символами нижнего регистра и цифрами.");
            } else Log.e("MV", "Домен второго уровня и домен верхнего уровня отсутствуют.");
        } else Log.e("MV", "Входная строка не содержит символ @.");
        // если входная строка не проходит проверки
        // возвращаем false
        return false;
    }
    public static boolean symbolValidation(String value, boolean number) {
        char[] charValues = value.toCharArray();
        boolean boolValues = true;
        for(int i = 0; i < charValues.length; i++) {
            if(charValues[i] != '0' &&
                    charValues[i] != '1' &&
                    charValues[i] != '2' &&
                    charValues[i] != '3' &&
                    charValues[i] != '4' &&
                    charValues[i] != '5' &&
                    charValues[i] != '6' &&
                    charValues[i] != '7' &&
                    charValues[i] != '8' &&
                    charValues[i] != '9' &&
                    number) {

                if(!Character.isLowerCase(charValues[i])) {
                    boolValues = false;
                    break;
                }
            } else if(!number) {
                if(!Character.isLowerCase(charValues[i])) {
                    boolValues = false;
                    break;
                }
            }
        }
        return boolValues;
    }
}

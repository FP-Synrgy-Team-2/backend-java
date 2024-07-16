package com.example.jangkau.utils;

import java.util.Random;

public class NumberGeneratorUtil {
    public static String generateNumber(int length) {
        Random random = new Random();
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("40");
        for (int i = 2; i<length; i++) {
            int digit = random.nextInt(10);
            stringBuilder.append(digit);
        }

        return stringBuilder.toString();
    }
}

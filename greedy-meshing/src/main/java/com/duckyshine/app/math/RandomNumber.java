package com.duckyshine.app.math;

import java.util.Random;

public class RandomNumber {
    private static final Random RANDOM = new Random();

    public static int getRandomInteger(int bound) {
        return RandomNumber.RANDOM.nextInt(bound);
    }
}

package com.example.twojepiwo.Beers;

import java.util.Random;

public class BeersUtils {

    public static String chooseNextDailyBeer()
    {
        String[] allBeers = {"Harnold","Perla","Tatra"};
        Random random = new Random();
        int randomNumber = random.nextInt(allBeers.length);
        return allBeers[randomNumber];
    }
}

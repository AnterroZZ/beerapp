package com.example.twojepiwo;

public class AddNewBeerMath {

    public static float AccountLevelProgress(float currentProgress, int ammount, int percatange, int currentLevel,boolean dailyBeer)
    {
        float multiplayer;
        if(dailyBeer)
        {
            multiplayer = (float) 1.5;
        }else multiplayer = 1;
        return currentProgress + ammount / 5000F / currentLevel * percatange * multiplayer;
    }
}

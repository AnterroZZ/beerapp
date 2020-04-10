package com.example.twojepiwo.Beers;

public class Beers {
    private int harnold;
    private int tatra;
    private int perla;

    public Beers()
    {
        harnold = 0;
        tatra = 0;
        perla = 0;
    }

    public Beers(int harnoldValue,int tatraValue, int perlaValue)
    {
        this.harnold = harnoldValue;
        this.tatra = tatraValue;
        this.perla = perlaValue;
    }

    public void setHarnold(int harnold) {
        this.harnold = harnold;
    }

    public void setTatra(int tatra) {
        this.tatra = tatra;
    }

    public void setPerla(int perla) {
        this.perla = perla;
    }

    public int getHarnold() {
        return harnold;
    }

    public int getTatra() {
        return tatra;
    }

    public int getPerla() {
        return perla;
    }
}

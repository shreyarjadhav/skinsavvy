package com.cs407.skincare;

public class Ingredient {
    private long id;
    private String name;
    private int acne;
    private int oily;
    private int dry;
    private int combo;

    public Ingredient(long id, String name, int acne, int oily, int dry, int combo) {
        this.id = id;
        this.name = name;
        this.acne = acne;
        this.oily = oily;
        this.dry = dry;
        this.combo = combo;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getAcne() {
        return acne;
    }

    public int getOily() {
        return oily;
    }

    public int getDry() {
        return dry;
    }

    public int getCombo() {
        return combo;
    }
}

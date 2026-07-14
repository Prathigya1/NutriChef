package com.example.nutrichef;

public class Recipe {

    private String title;
    private String ingredients;
    private String steps;
    private String nutrition;
    private int imageResId;
    private boolean isPopular = false; // default

    public Recipe(String title, String ingredients, String steps, String nutrition, int imageResId) {
        this.title = title;
        this.ingredients = ingredients;
        this.steps = steps;
        this.nutrition = nutrition;
        this.imageResId = imageResId;
    }

    // optional convenience constructor if you want to set popular at creation
    public Recipe(String title, String ingredients, String steps, String nutrition, int imageResId, boolean isPopular) {
        this(title, ingredients, steps, nutrition, imageResId);
        this.isPopular = isPopular;
    }

    public String getTitle() { return title; }
    public String getIngredients() { return ingredients; }
    public String getSteps() { return steps; }
    public String getNutrition() { return nutrition; }
    public int getImageResId() { return imageResId; }
    public boolean isPopular() { return isPopular; }

    public void setPopular(boolean popular) { this.isPopular = popular; }
}



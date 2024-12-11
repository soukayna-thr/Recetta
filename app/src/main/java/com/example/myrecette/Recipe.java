package com.example.myrecette;
public class Recipe {
    private String name;
    private String ingredients;
    private String steps;
    private String photoUri;
    private String category;

    public Recipe(String name, String ingredients, String steps, String photoUri, String category) {
        this.name = name;
        this.ingredients = ingredients;
        this.steps = steps;
        this.photoUri = photoUri;
        this.category = category;
    }

    public String getName() { return name; }
    public String getIngredients() { return ingredients; }
    public String getSteps() { return steps; }
    public String getPhotoUri() { return photoUri; }
    public String getCategory() { return category; }
}

package com.rezdy.lunch.dto;


import java.io.Serializable;
import java.util.Set;

public class RecipeDTO {

    private String title;
    private Set<IngredientDTO> ingredients;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Set<IngredientDTO> getIngredients() {
        return ingredients;
    }

    public void setIngredients(Set<IngredientDTO> ingredients) {
        this.ingredients = ingredients;
    }
}

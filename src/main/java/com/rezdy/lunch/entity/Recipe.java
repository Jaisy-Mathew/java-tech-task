package com.rezdy.lunch.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Recipe implements Serializable{
    private static final long serialVersionUID = 1L;

    @Id
    private String title;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "recipe_ingredient",
            joinColumns = {@JoinColumn(name="recipe", referencedColumnName = "title")},
            inverseJoinColumns = {@JoinColumn(name="ingredient", referencedColumnName = "title")}
    )
    private Set<Ingredient> ingredients= new HashSet<>();

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Set<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(Set<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

}

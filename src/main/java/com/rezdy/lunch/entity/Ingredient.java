package com.rezdy.lunch.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Ingredient implements Serializable{
    private static final long serialVersionUID = 1L;
    @Id
    private String title;

    private LocalDate bestBefore;

    private LocalDate useBy;

    @ManyToMany(mappedBy = "ingredients", fetch = FetchType.EAGER)
    private Set<Recipe> recipes = new HashSet<>();;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDate getBestBefore() {
        return bestBefore;
    }

    public void setBestBefore(LocalDate bestBefore) {
        this.bestBefore = bestBefore;
    }

    public LocalDate getUseBy() {
        return useBy;
    }

    public void setUseBy(LocalDate useBy) {
        this.useBy = useBy;
    }

    public Set<Recipe> getRecipes() {
        return recipes;
    }

    public void setRecipes(Set<Recipe> recipes) {
        this.recipes = recipes;
    }

}

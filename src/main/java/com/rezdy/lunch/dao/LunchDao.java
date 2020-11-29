package com.rezdy.lunch.dao;

import com.rezdy.lunch.dto.IngredientDTO;
import com.rezdy.lunch.dto.RecipeDTO;
import com.rezdy.lunch.entity.Ingredient;
import com.rezdy.lunch.entity.Recipe;
import com.rezdy.lunch.repository.LunchRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Component
public class LunchDao {

    @Autowired
    private LunchRepository lunchRepository;

    /**
     * interacts with database repository to get the list of recipes based on ingredient availability
     * @param date
     * @return List<RecipeDTO>
     */
    public List<RecipeDTO> loadRecipes(LocalDate date) {
        List<Recipe> recipes = lunchRepository.findNonExpiredRecipeList(date);
        List<RecipeDTO> recipeDTOs = new ArrayList<>();
        Iterator recipeIterator = recipes.listIterator();
        while (recipeIterator.hasNext()) {
            Recipe recipe = (Recipe) recipeIterator.next();
            RecipeDTO recipeDTO = new RecipeDTO();
            BeanUtils.copyProperties(recipe, recipeDTO);
            List<IngredientDTO> ingredientDTOs = new ArrayList<>();
            if(!CollectionUtils.isEmpty(recipe.getIngredients())) {
                Iterator ingredientIterator = recipe.getIngredients().iterator();
                while (ingredientIterator.hasNext()) {
                    Ingredient ingredient = (Ingredient) ingredientIterator.next();
                    IngredientDTO ingredientDTO = new IngredientDTO();
                    BeanUtils.copyProperties(ingredient, ingredientDTO);
                    ingredientDTOs.add(ingredientDTO);
                    ingredientIterator.remove();
                }
            }
            recipeDTO.getIngredients().addAll(ingredientDTOs);
            recipeDTOs.add(recipeDTO);
        }

        return recipeDTOs;
    }
}

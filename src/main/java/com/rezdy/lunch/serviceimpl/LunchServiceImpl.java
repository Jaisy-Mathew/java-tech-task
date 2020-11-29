package com.rezdy.lunch.serviceimpl;

import com.rezdy.lunch.dao.LunchDao;
import com.rezdy.lunch.dto.IngredientDTO;
import com.rezdy.lunch.dto.RecipeDTO;
import com.rezdy.lunch.exception.ServiceException;
import com.rezdy.lunch.service.LunchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LunchServiceImpl implements LunchService {

    @Autowired
    private LunchDao lunchDao;

    private List<RecipeDTO> recipesSorted;
    private static final Logger LOG = LoggerFactory.getLogger(LunchServiceImpl.class);

    /**
     *  gets the list of recies using non expired ingredients and sorted based on use-by and best before dates
     * @param date
     * @return List<RecipeDTO>
     */
    @Override
    public List<RecipeDTO> getNonExpiredRecipesOnDate(LocalDate date) {
        try{
            List<RecipeDTO> recipes  = lunchDao.loadRecipes(date);
            if(!CollectionUtils.isEmpty(recipes)) {
                LOG.info("available non expired recipe list size: , {}", recipes.size());
                sortRecipes(recipes, date);
            }else{
                this.recipesSorted = new ArrayList<>();
            }
            return this.recipesSorted;
        }catch(Exception e){
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * sorts the list of recipes by using orderd list comparing the given date with best before and use by dates
     * @param recipes
     * @param date
     */
    private void sortRecipes(List<RecipeDTO> recipes, LocalDate date) {
        try{
            List<RecipeDTO> recipesPastBestBeforeDate = new ArrayList<>();
            List<RecipeDTO> nonExpiredIngredientRecipes = new ArrayList<>();
            for (RecipeDTO recipe : recipes){
                LOG.info("recipe title is: , {}", recipe.getTitle());
               List<IngredientDTO> ingredients = recipe.getIngredients().stream()
                       .filter(recipeIngredient
                               -> (date.isAfter(recipeIngredient.getBestBefore())
                               && date.isBefore(recipeIngredient.getUseBy()))).collect(Collectors.toList());
               if(CollectionUtils.isEmpty(ingredients)){
                   nonExpiredIngredientRecipes.add(recipe);
               }else {
                   recipesPastBestBeforeDate.add(recipe);
               }
            }
            this.recipesSorted = new ArrayList<>();
            this.recipesSorted.addAll(nonExpiredIngredientRecipes);
            this.recipesSorted.addAll(recipesPastBestBeforeDate);
        }catch(Exception e){
            throw new ServiceException(e.getMessage());
        }
    }
}

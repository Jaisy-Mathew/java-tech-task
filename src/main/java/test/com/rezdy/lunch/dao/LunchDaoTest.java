package test.com.rezdy.lunch.dao;

import com.rezdy.lunch.LunchApplication;
import com.rezdy.lunch.dao.LunchDao;
import com.rezdy.lunch.dto.IngredientDTO;
import com.rezdy.lunch.dto.RecipeDTO;
import com.rezdy.lunch.entity.Ingredient;
import com.rezdy.lunch.entity.Recipe;
import com.rezdy.lunch.repository.LunchRepository;
import org.json.JSONException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.validation.constraints.AssertTrue;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = LunchApplication.class)
@ActiveProfiles("test")
public class LunchDaoTest {

    @MockBean
    private LunchRepository repository;

    @Autowired
    private LunchDao lunchDao;

    @Test
    public void find_Non_Expired_Recipe_OK()  {
        when(repository.findNonExpiredRecipeList(LocalDate.of(2020, 01,01))).thenReturn(buildNonExpiredRecipes());
        List<RecipeDTO> recipes = lunchDao.loadRecipes(LocalDate.of(2020, 01,01));
        verify(repository, times(1)).findNonExpiredRecipeList(LocalDate.of(2020, 01,01));
        assertEquals(1,recipes.size());
    }

    @Test
    public void find_Non_Expired_Recipe_Sorted_OK()  {
        when(repository.findNonExpiredRecipeList(LocalDate.of(2020, 01,01))).thenReturn(buildNonExpiredSortedRecipes());
        List<RecipeDTO> recipes = lunchDao.loadRecipes(LocalDate.of(2020, 01,01));
        verify(repository, times(1)).findNonExpiredRecipeList(LocalDate.of(2020, 01,01));
        assertEquals(1,recipes.size());
    }

    private List<Recipe> buildNonExpiredRecipes() {
        List<Recipe>  recipes= new ArrayList<>();
        Recipe recipe = new Recipe();
        recipe.setTitle("omlette");
        Ingredient ingredient1 = new Ingredient();
        ingredient1.setTitle("egg");
        ingredient1.setUseBy(LocalDate.of(2030,01,01));
        ingredient1.setBestBefore(LocalDate.of(2030, 01,01));
        Ingredient ingredient2 = new Ingredient();
        ingredient2.setTitle("pepper");
        ingredient2.setUseBy(LocalDate.of(2029,01,20));
        ingredient2.setBestBefore(LocalDate.of(2025, 01,01));
        Set<Ingredient> ingredients = new HashSet<>();
        ingredients.add(ingredient1);
        ingredients.add(ingredient2);
        recipe.getIngredients().addAll(ingredients);
        recipes.add(recipe);
        return recipes;
    }
    private List<Recipe> buildNonExpiredSortedRecipes() {
        List<Recipe>  recipes= new ArrayList<>();
        Recipe recipe = new Recipe();
        recipe.setTitle("omlette");
        Ingredient ingredient1 = new Ingredient();
        ingredient1.setTitle("egg");
        ingredient1.setBestBefore(LocalDate.of(2030, 01,01));
        ingredient1.setUseBy(LocalDate.of(2030,01,01));
        Ingredient ingredient2 = new Ingredient();
        ingredient2.setTitle("pepper");
        ingredient2.setBestBefore(LocalDate.of(2025, 01,01));
        ingredient2.setUseBy(LocalDate.of(2029,01,20));
        Ingredient ingredient3 = new Ingredient();
        ingredient3.setTitle("salt");
        ingredient3.setBestBefore(LocalDate.of(2019, 01,01));
        ingredient3.setUseBy(LocalDate.of(2030,01,20));
        Set<Ingredient> ingredients = new HashSet<>();
        ingredients.add(ingredient3);
        ingredients.add(ingredient1);
        ingredients.add(ingredient2);
        recipe.getIngredients().addAll(ingredients);
        recipes.add(recipe);
        return recipes;
    }

}
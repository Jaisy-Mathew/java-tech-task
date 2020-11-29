package test.com.rezdy.lunch.controller;

import com.rezdy.lunch.LunchApplication;
import com.rezdy.lunch.dao.LunchDao;
import com.rezdy.lunch.dto.IngredientDTO;
import com.rezdy.lunch.dto.RecipeDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.*;

import org.json.JSONException;
import static org.mockito.Mockito.*;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = LunchApplication.class) // for restTemplate
@ActiveProfiles("test")
public class LunchControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @MockBean
    private LunchDao lunchDao;

    @Test
    public void find_Non_Expired_Recipe_OK() throws JSONException {
        when(lunchDao.loadRecipes(LocalDate.of(2020, 01,01))).thenReturn(buildNonExpiredRecipes());

        ResponseEntity<String> response = restTemplate.getForEntity("/lunch?date=2020-01-01", String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());
        verify(lunchDao, times(1)).loadRecipes(LocalDate.of(2020, 01,01));
    }

    @Test
    public void find_Non_Expired_Recipe_Sorted_OK() throws JSONException {
        when(lunchDao.loadRecipes(LocalDate.of(2020, 01,01))).thenReturn(buildNonExpiredSortedRecipes());

        ResponseEntity<String> response = restTemplate.getForEntity("/lunch?date=2020-01-01", String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());
        verify(lunchDao, times(1)).loadRecipes(LocalDate.of(2020, 01,01));
    }
    @Test
    public void find_All_Expired_Recipe_OK() throws JSONException {
        when(lunchDao.loadRecipes(LocalDate.of(2020, 01,01))).thenReturn(new ArrayList<>());
        String expected = "[]";

        ResponseEntity<String> response = restTemplate.getForEntity("/lunch?date=2020-01-01", String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());
        JSONAssert.assertEquals(expected, response.getBody(), false);
        verify(lunchDao, times(1)).loadRecipes(LocalDate.of(2020, 01,01));
    }
    @Test
    public void invalid_HTTP_Method_405() throws JSONException {
        String patchInJson = "{\"date\":\"2020-01-01\"}";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(patchInJson, headers);

        ResponseEntity<String> response = restTemplate.exchange("/lunch", HttpMethod.POST, entity, String.class);

        assertEquals(HttpStatus.METHOD_NOT_ALLOWED, response.getStatusCode());

        verify(lunchDao, times(0)).loadRecipes(LocalDate.of(2020, 01,01));
    }
    @Test
    public void invalid_Request_400() throws Exception {

        ResponseEntity<String> response = restTemplate.getForEntity("/lunch", String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

    }
    private List<RecipeDTO> buildNonExpiredRecipes() {
        List<RecipeDTO>  recipes= new ArrayList<>();
        RecipeDTO recipe = new RecipeDTO();
        recipe.setTitle("omlette");
        IngredientDTO ingredient1 = new IngredientDTO();
        ingredient1.setTitle("egg");
        ingredient1.setUseBy(LocalDate.of(2030,01,01));
        ingredient1.setBestBefore(LocalDate.of(2030, 01,01));
        IngredientDTO ingredient2 = new IngredientDTO();
        ingredient2.setTitle("pepper");
        ingredient2.setUseBy(LocalDate.of(2029,01,20));
        ingredient2.setBestBefore(LocalDate.of(2025, 01,01));
        Set<IngredientDTO> ingredients = new HashSet<>();
        ingredients.add(ingredient1);
        ingredients.add(ingredient2);
        recipe.setIngredients(ingredients);
        recipes.add(recipe);
        return recipes;
    }

    private List<RecipeDTO> buildNonExpiredSortedRecipes() {
        List<RecipeDTO>  recipes= new ArrayList<>();
        RecipeDTO recipe = new RecipeDTO();
        recipe.setTitle("omlette");
        IngredientDTO ingredient1 = new IngredientDTO();
        ingredient1.setTitle("egg");
        ingredient1.setBestBefore(LocalDate.of(2030, 01,01));
        ingredient1.setUseBy(LocalDate.of(2030,01,01));
        IngredientDTO ingredient2 = new IngredientDTO();
        ingredient2.setTitle("pepper");
        ingredient2.setBestBefore(LocalDate.of(2025, 01,01));
        ingredient2.setUseBy(LocalDate.of(2029,01,20));
        IngredientDTO ingredient3 = new IngredientDTO();
        ingredient3.setTitle("salt");
        ingredient3.setBestBefore(LocalDate.of(2019, 01,01));
        ingredient3.setUseBy(LocalDate.of(2030,01,20));
        Set<IngredientDTO> ingredients = new HashSet<>();
        ingredients.add(ingredient3);
        ingredients.add(ingredient1);
        ingredients.add(ingredient2);
        recipe.setIngredients(ingredients);
        recipes.add(recipe);
        return recipes;
    }

}
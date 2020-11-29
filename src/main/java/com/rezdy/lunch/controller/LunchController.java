package com.rezdy.lunch.controller;

import com.rezdy.lunch.dto.RecipeDTO;
import com.rezdy.lunch.service.LunchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
public class LunchController {

    private LunchService lunchService;

    @Autowired
    public LunchController(LunchService lunchService) {
        this.lunchService = lunchService;
    }

    /**
     *
     * @param date
     * @return List<RecipeDTO>
     */
    @GetMapping("/lunch")
    public List<RecipeDTO> getRecipes(@RequestParam(value = "date") String date) {

        return lunchService.getNonExpiredRecipesOnDate(LocalDate.parse(date));
    }
}

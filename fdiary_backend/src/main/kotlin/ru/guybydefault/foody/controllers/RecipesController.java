package ru.guybydefault.foody.controllers;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.guybydefault.foody.domain.Recipe;
import ru.guybydefault.foody.domain.User;
import ru.guybydefault.foody.dtos.RecipeDto;
import ru.guybydefault.foody.service.RecipeService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/recipes")
public class RecipesController {
    private final RecipeService recipeService;

    public RecipesController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @GetMapping
    public Iterable<Recipe> findRecipesByCriteria(
            @RequestParam(value = "caloriesMin", required = false) Integer caloriesMin,
            @RequestParam(value = "caloriesMax", required = false) Integer caloriesMax,
            @RequestParam(value = "cuisine", required = false) String cuisine,
            @RequestParam(value = "category", required = false) String category,
            @RequestParam(value = "minRating", required = false) Integer rating,
            @RequestParam(value = "maxTimePrepared", required = false) Integer maxTimePrepared
    ) {
        return recipeService.findAll(caloriesMin, caloriesMax, cuisine, category, rating, maxTimePrepared);
    }

    @PostMapping
    public ResponseEntity<Recipe> createRecipe(@ModelAttribute("user") User user, @RequestBody @Valid  RecipeDto recipe) {
        return ResponseEntity.ok(recipeService.saveRecipe(recipe, user));
    }

    @GetMapping("/cuisines")
    public List<String> cuisines() {
        return recipeService.getCuisines();
    }

    @GetMapping("/categories")
    public List<String> categories() {
        return recipeService.getCategories();
    }
}

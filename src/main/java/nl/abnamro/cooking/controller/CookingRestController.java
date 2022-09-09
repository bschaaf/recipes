package nl.abnamro.cooking.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import nl.abnamro.cooking.controller.mapping.DTOMappers;
import nl.abnamro.cooking.controller.mapping.MessageResponse;
import nl.abnamro.cooking.model.Recipe;
import nl.abnamro.cooking.model.dto.IngredientDTO;
import nl.abnamro.cooking.model.dto.IngredientsDTO;
import nl.abnamro.cooking.model.dto.RecipeDTO;
import nl.abnamro.cooking.model.dto.RecipesDTO;
import nl.abnamro.cooking.service.RecipeService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.stream.Collectors;

import static nl.abnamro.cooking.controller.mapping.DTOMappers.*;

/**
 * Defines the REST API interface to exchange information with the RecipeService.
 *
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("api/cooking/v1")
public class CookingRestController {
    private final RecipeService recipeService;

    @Operation(summary = "Create a recipe")
    @Tag(name = "1 Create")
    @RequestMapping(value = "/recipe", produces = {"application/json",
            "application/xml"}, method = RequestMethod.POST)
    RecipeDTO createRecipe(@RequestBody RecipeDTO recipeDTO) {
        Recipe recipe = recipeService.createRecipe(map2Recipe(recipeDTO));
        recipeDTO.getIngredients().forEach(ingredientDTO -> recipeService.createIngredient(map2Ingredient(ingredientDTO),recipe));
        return map2RecipeDTO(recipe);
    }

    @Operation(summary = "Create an ingredient for a recipe")
    @Tag(name = "1 Create")
    @RequestMapping(value = "/recipe/{id}/ingredient", produces = {"application/json", "application/xml"}, method = RequestMethod.POST)
    IngredientDTO createIngredient(@RequestBody IngredientDTO ingredientDTO, @PathVariable Integer id) {
        return map2IngredientDTO(recipeService.createIngredient(map2Ingredient(ingredientDTO), id));
    }

    @Operation(summary = "Get a recipe by its id")
    @Tag(name = "2 Read")
    @RequestMapping(value = "/recipe/{id}", produces = {"application/json",
            "application/xml"}, method = RequestMethod.GET)
    RecipeDTO getRecipe(@PathVariable Integer id) {
        return map2RecipeDTO(recipeService.getRecipe(id));
    }

    @Operation(summary = "Get an ingredient by its id")
    @Tag(name = "2 Read")
    @RequestMapping(value = "/ingredient/{id}", produces = {"application/json",
            "application/xml"}, method = RequestMethod.GET)
    IngredientDTO getIngredient(@PathVariable Integer id) {
        return map2IngredientDTO(recipeService.getIngredient(id));
    }

    @Operation(summary = "Get an ingredient by its name")
    @Tag(name = "2 Read")
    @RequestMapping(value = "/ingredient/byName/{name}", produces = {"application/json",
            "application/xml"}, method = RequestMethod.GET)
    IngredientsDTO getIngredient(@PathVariable String name) {
        return new IngredientsDTO(recipeService.getIngredientByName(name).stream()
                .map(DTOMappers::map2IngredientDTO)
                .sorted(Comparator.comparingInt(IngredientDTO::getId))
                .collect(Collectors.toCollection(LinkedHashSet::new)));
    }

    @Operation(summary = "Get a list of all recipes")
    @Tag(name = "2 Read")
    @RequestMapping(value = "/recipes", produces = {"application/json",
            "application/xml"}, method = RequestMethod.GET)
    RecipesDTO getRecipes() {
        return new RecipesDTO(recipeService.getRecipes().stream()
                .map(DTOMappers::map2RecipeDTO)
                .sorted(Comparator.comparingInt(RecipeDTO::getId))
                .collect(Collectors.toCollection(LinkedHashSet::new)));
    }

    @Operation(summary = "Get a list of filtered recipes. Filter by request parameters.")
    @Tag(name = "2 Read")
    @RequestMapping(value = "/recipes/filter", produces = {"application/json",
            "application/xml"}, method = RequestMethod.GET)
    RecipesDTO getRecipesByParameters(@RequestParam(required = false) String include, @RequestParam(required = false) String exclude,
                                      @RequestParam(required = false) String instructions, @RequestParam(required = false) Integer servings,
                                      @RequestParam(required = false) String vegetarian) {
        return new RecipesDTO(recipeService.getRecipes(include, exclude, instructions, servings, vegetarian).stream()
                .map(DTOMappers::map2RecipeDTO)
                .sorted(Comparator.comparingInt(RecipeDTO::getId))
                .collect(Collectors.toCollection(LinkedHashSet::new)));
    }

    @Operation(summary = "Get a list of recipes which match name")
    @Tag(name = "2 Read")
    @RequestMapping(value = "/recipes/byName/{name}", produces = {"application/json",
            "application/xml"}, method = RequestMethod.GET)
    RecipesDTO getRecipesByName(@PathVariable String name) {
        return new RecipesDTO(recipeService.getRecipesByName(name).stream()
                .map(DTOMappers::map2RecipeDTO)
                .sorted(Comparator.comparingInt(RecipeDTO::getId))
                .collect(Collectors.toCollection(LinkedHashSet::new)));
    }

    @Operation(summary = "Get a list of all ingredients")
    @Tag(name = "2 Read")
    @RequestMapping(value = "/ingredients", produces = {"application/json", "application/xml"}, method = RequestMethod.GET)
    IngredientsDTO getIngredients() {
        return new IngredientsDTO(recipeService.getIngredients().stream()
                .map(DTOMappers::map2IngredientDTO)
                .sorted(Comparator.comparingInt(IngredientDTO::getId))
                .collect(Collectors.toCollection(LinkedHashSet::new)));
    }

    @Operation(summary = "Get a list of ingredients by recipe id")
    @Tag(name = "2 Read")
    @RequestMapping(value = "/ingredients/byRecipe/{id}", produces = {"application/json",
            "application/xml"}, method = RequestMethod.GET)
    IngredientsDTO getIngredientsByRecipe(@PathVariable Integer id) {
        return new IngredientsDTO(recipeService.getIngredientsByRecipe(id).stream()
                .map(DTOMappers::map2IngredientDTO)
                .sorted(Comparator.comparingInt(IngredientDTO::getId))
                .collect(Collectors.toCollection(LinkedHashSet::new)));
    }

    @Operation(summary = "Update a recipe")
    @Tag(name = "3 Update")
    @RequestMapping(value = "/recipe", produces = {"application/json",
            "application/xml"}, method = RequestMethod.PUT)
    RecipeDTO updateRecipe(@RequestBody RecipeDTO recipeDTO) {
        return map2RecipeDTO(recipeService.updateRecipe(map2Recipe(recipeDTO), recipeDTO.getId()));
    }

    @Operation(summary = "Update an ingredient")
    @Tag(name = "3 Update")
    @RequestMapping(value = "/ingredient", produces = {"application/json",
            "application/xml"}, method = RequestMethod.PUT)
    IngredientDTO updateIngredient(@RequestBody IngredientDTO ingredientDTO) {
        return map2IngredientDTO(recipeService.updateIngredient(map2Ingredient(ingredientDTO), ingredientDTO.getId()));
    }

    @Operation(summary = "Delete a recipe by id")
    @Tag(name = "4 Delete")
    @RequestMapping(value = "/recipe/{id}", produces = {"application/json",
            "application/xml"}, method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.ACCEPTED)
    MessageResponse deleteRecipe(@PathVariable Integer id) {
        return recipeService.deleteRecipe(id);
    }

    @Operation(summary = "Delete an ingredient by id")
    @Tag(name = "4 Delete")
    @RequestMapping(value = "/ingredient/{id}", produces = {"application/json",
            "application/xml"}, method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.ACCEPTED)
    MessageResponse deleteIngredient(@PathVariable Integer id) {
        return recipeService.deleteIngredient(id);
    }
}

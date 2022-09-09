package nl.abnamro.cooking.controller.mapping;

import nl.abnamro.cooking.model.Ingredient;
import nl.abnamro.cooking.model.Recipe;
import nl.abnamro.cooking.model.dto.IngredientDTO;
import nl.abnamro.cooking.model.dto.RecipeDTO;

import java.util.stream.Collectors;

/**
 * Mappers used in the controller layer to wrap and unwrap data objects used by the api
 */
public class DTOMappers {
    public static RecipeDTO map2RecipeDTO(Recipe recipe) {
        return RecipeDTO.builder()
                .id(recipe.getRecipeId())
                .name(recipe.getName())
                .genre(recipe.getGenre())
                .instructions(recipe.getInstructions())
                .servings(recipe.getServings())
                .ingredients(recipe.getIngredients().stream()
                        .map(DTOMappers::map2IngredientDTO)
                        .collect(Collectors.toList()))
                .build();
    }

    public static IngredientDTO map2IngredientDTO(Ingredient ingredient) {
        IngredientDTO ingredientDTO =
         IngredientDTO.builder()
                .id(ingredient.getIngredientId())
                .name(ingredient.getName())
                .quantity(ingredient.getQuantity())
                .type(ingredient.getType())
                .build();
        return ingredientDTO;
    }

    public static Recipe map2Recipe(RecipeDTO recipeDTO) {
        final Recipe recipe = Recipe.builder()
                .name(recipeDTO.getName())
                .genre(recipeDTO.getGenre())
                .instructions(recipeDTO.getInstructions())
                .servings(recipeDTO.getServings())
                .ingredients(recipeDTO.getIngredients().stream()
                        .map(DTOMappers::map2Ingredient)
                        .collect(Collectors.toList()))
                .build();

        return recipe;
    }

    public static Ingredient map2Ingredient(IngredientDTO ingredient) {
        return Ingredient.builder()
                .name(ingredient.getName())
                .quantity(ingredient.getQuantity())
                .type(ingredient.getType())
                .build();
    }

}

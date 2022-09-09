package nl.abnamro.cooking.service;

import nl.abnamro.cooking.model.Ingredient;
import nl.abnamro.cooking.model.IngredientRepository;
import nl.abnamro.cooking.model.Recipe;
import nl.abnamro.cooking.model.RecipeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CookingServiceCreateTest {
    @Mock
    private IngredientRepository ingredientRepository;
    @Mock
    private RecipeRepository recipeRepository;

    @InjectMocks
    private RecipeService recipeService;

    @Test
    void givenIngredientAndRecipeWhenCreateThenReturnIngredient() {
        // Given a ingredient and recipe
        var ingredient = new Ingredient();
        var recipe = new Recipe();
        // When ingredient is created
        when(ingredientRepository.save(any(Ingredient.class))).thenAnswer(returnsFirstArg());
        var ingredientCreated = recipeService.createIngredient(ingredient, recipe);
        // Then a ingredient is returned which refers to recipe
        assertNotNull(ingredientCreated);
        assertTrue(ingredientCreated.getRecipe() == recipe);
    }

    @Test
    void givenIngredientAndRecipeWithIdWhenCreateThenReturnIngredient() {
        // Given a ingredient and recipe
        var ingredient = new Ingredient();
        var recipe = new Recipe();
        recipe.setRecipeId(10);
        // When ingredient is created
        when(recipeRepository.findById(any(Integer.class))).thenReturn(Optional.of(recipe));
        when(ingredientRepository.save(any(Ingredient.class))).thenAnswer(returnsFirstArg());
        var ingredientCreated = recipeService.createIngredient(ingredient, 10);
        // Then a ingredient is returned which refers to recipe
        assertNotNull(ingredientCreated);
        assertTrue(ingredientCreated.getRecipe() == recipe);
    }

    // etc.
}

package nl.abnamro.cooking.service;

import nl.abnamro.cooking.controller.exceptions.EntityNotFoundException;
import nl.abnamro.cooking.model.Ingredient;
import nl.abnamro.cooking.model.IngredientRepository;
import nl.abnamro.cooking.model.Recipe;
import nl.abnamro.cooking.model.RecipeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.internal.stubbing.defaultanswers.ReturnsEmptyValues;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Optional;

import static nl.abnamro.cooking.config.GlobalStringResources.ENTITY_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CookingServiceUpdateTest {
    @Mock
    private IngredientRepository ingredientRepository;
    @Mock
    private RecipeRepository recipeRepository;

    @InjectMocks
    private RecipeService recipeService;

    @Test
    void givenRecipeWhenRecipeIsUpdatedAndNotFoundThenExceptionIsThrown() {
        // Given a ingredient id and ingredient dto
        var id = 10;
        var recipe = new Recipe();
        // When the ingredient is not found in the ingredientRepository
        when(recipeRepository.findById(10)).then(new ReturnsEmptyValues());

        // Then an exception is thrown
        var exception = assertThrows(EntityNotFoundException.class, () -> recipeService.updateRecipe(recipe, id));

        var expectedMessage = ENTITY_NOT_FOUND + id;
        var actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void givenRecipeAndUpdateWhenUpdatedThenReturnsUpdatedRecipe() {
        // Given an existing recipe and a recipe update
        var id = 10;

        var existingRecipe = Recipe.builder().name("citroen kip").ingredients(new ArrayList<>())
                .instructions("Bekleed de kip met citroen en bak in de oven.").servings(4).build();
        var recipeUpdate = Recipe.builder().name("citroen kip")
                .instructions("Bekleed de kip met citroen, voeg een snufje zout toe en bak in de oven.").build();

        // When the ingredient is found in the ingredientRepository and updated
        when(recipeRepository.findById(id)).thenReturn(Optional.of(existingRecipe));
        when(recipeRepository.save(any(Recipe.class))).thenAnswer(returnsFirstArg());

        var recipeUpdated = recipeService.updateRecipe(recipeUpdate, id);

        // Then an updated ingredient is returned and fields not updated are as before.
        assertNotNull(recipeUpdated);
        assertTrue("citroen kip".equals(recipeUpdated.getName()));
        assertTrue(recipeUpdated.getInstructions().contains("een snufje zout"));
        assertTrue(recipeUpdated.getServings() == 4);
    }

    @Test
    void givenIngredientWhenIngredientIsUpdatedAndNotFoundThenExceptionIsThrown() {
        // Given a ingredient id and ingredient dto
        var id = 10;
        var ingredient = new Ingredient();
        // When the ingredient is not found in the ingredientRepository
        when(ingredientRepository.findById(10)).then(new ReturnsEmptyValues());

        // Then an exception is thrown
        var exception = assertThrows(EntityNotFoundException.class, () -> recipeService.updateIngredient(ingredient, id));

        var expectedMessage = ENTITY_NOT_FOUND + id;
        var actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void givenIngredientAndUpdateWhenUpdatedThenReturnsUpdatedIngredient() {
        // Given an existing ingredient and an ingredient update
        var id = 10;
        var existingIngredient = Ingredient.builder().name("kip").quantity("een hele").type("gevogelte").build();
        var ingredientUpdate = Ingredient.builder().name("kip").quantity("een halve").build();

        // When the ingredient is found in the ingredientRepository and updated
        when(ingredientRepository.findById(10)).thenReturn(Optional.of(existingIngredient));
        when(ingredientRepository.save(any(Ingredient.class))).thenAnswer(returnsFirstArg());
        var ingredientUpdated = recipeService.updateIngredient(ingredientUpdate, id);

        // Then an updated ingredient is returned and fields not updated are as before.
        assertNotNull(ingredientUpdated);
        assertTrue("kip".equals(ingredientUpdated.getName()));
        assertTrue("een halve".equals(ingredientUpdated.getQuantity()));
        assertTrue("gevogelte".equals(ingredientUpdated.getType()));
    }
}

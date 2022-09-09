package nl.abnamro.cooking.service;

import nl.abnamro.cooking.controller.exceptions.EntityNotDeletedException;
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
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Optional;

import static nl.abnamro.cooking.config.GlobalStringResources.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CookingServiceDeleteTest {
    @Mock
    private IngredientRepository ingredientRepository;
    @Mock
    private RecipeRepository recipeRepository;

    @InjectMocks
    private RecipeService recipeService;

    @Test
    void givenIdWhenDeleteUnknownRecipeThenThrowException() {
        // Given an id
        var id = 10;

        // When the ingredient is not found in the ingredientRepository
        when(recipeRepository.findById(10)).then(new ReturnsEmptyValues());

        // Then an exception is thrown
        var exception = assertThrows(
                EntityNotDeletedException.class, () -> recipeService.deleteIdFromRepository(id, recipeRepository));
        var expectedMessage = ENTITY_NOT_FOUND + id;
        var actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }
    @Test
    void givenIdWhenDeleteKnownRecipeConstraintViolationThenThrowException() {
        // Given an id
        var id = 10;

        // When the ingredient is found in the ingredientRepository
        when(recipeRepository.findById(10)).thenReturn(Optional.of(new Recipe()));
        doThrow(new DataIntegrityViolationException("")).when(recipeRepository).deleteById(10);

        // Then an exception is thrown
        var exception = assertThrows(
                EntityNotDeletedException.class, () -> recipeService.deleteIdFromRepository(id, recipeRepository));
        var expectedMessage = CONSTRAINT_VIOLATION_DELETE;
        var actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void givenIdAndRepositoryWhenDeleteKnownRecipeAndIdDeletedThenMessageSuccessful() {
        // Given an id
        var id = 10;
        // When the ingredient is found in the ingredientRepository
        when(recipeRepository.findById(10)).thenReturn(Optional.of(new Recipe())).then(new ReturnsEmptyValues());
        // Then a MessageResponse is generated
        var messageResponse = recipeService.deleteIdFromRepository(id, recipeRepository);
        var expectedMessage = DELETE_SUCCESFUL + id;
        var actualMessage = messageResponse.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }


    @Test
    void givenIdWhenDeleteUnknownIngredientThenThrowException() {
        // Given an id
        var id = 10;

        // When the ingredient is not found in the ingredientRepository
        when(ingredientRepository.findById(10)).then(new ReturnsEmptyValues());

        // Then an exception is thrown
        var exception = assertThrows(
                EntityNotDeletedException.class, () -> recipeService.deleteIdFromRepository(id, ingredientRepository));
        var expectedMessage = ENTITY_NOT_FOUND + id;
        var actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void givenIdWhenDeleteKnownIngredientConstraintViolationThenThrowException() {
        // Given an id
        var id = 10;

        // When the ingredient is found in the ingredientRepository
        when(ingredientRepository.findById(10)).thenReturn(Optional.of(new Ingredient()));
        doThrow(new DataIntegrityViolationException("")).when(ingredientRepository).deleteById(10);

        // Then an exception is thrown
        var exception = assertThrows(
                EntityNotDeletedException.class, () -> recipeService.deleteIdFromRepository(id, ingredientRepository));
                var expectedMessage = CONSTRAINT_VIOLATION_DELETE;
        var actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void givenIdAndRepositoryWhenDeleteKnownIngredientAndIdDeletedThenMessageSuccessful() {
        // Given an id
        var id = 10;

        // When the ingredient is found in the ingredientRepository
        when(ingredientRepository.findById(10)).thenReturn(Optional.of(new Ingredient())).then(new ReturnsEmptyValues());

        // Then a MessageResponse is generated
        var messageResponse = recipeService.deleteIdFromRepository(id, ingredientRepository);
        var expectedMessage = DELETE_SUCCESFUL + id;
        var actualMessage = messageResponse.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

}

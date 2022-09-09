package nl.abnamro.cooking.service;

import nl.abnamro.cooking.controller.exceptions.EntityNotFoundException;
import nl.abnamro.cooking.model.Ingredient;
import nl.abnamro.cooking.model.IngredientRepository;
import nl.abnamro.cooking.model.Recipe;
import nl.abnamro.cooking.model.RecipeRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.internal.stubbing.defaultanswers.ReturnsEmptyValues;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static nl.abnamro.cooking.config.GlobalStringResources.ENTITY_NOT_FOUND;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CookingServiceReadTest {
    @Mock
    private IngredientRepository ingredientRepository;
    @Mock
    private RecipeRepository recipeRepository;

    @InjectMocks
    private RecipeService recipeService;

    private static Recipe recipe1;
    private static Recipe recipe2;
    private static Recipe recipe3;

    @BeforeAll
    static void initRecipes(){
        recipe1 = Recipe.builder().recipeId(1)
                .name("spaghetti carbonara")
                .instructions("Kook de spaghetti. Klop de eieren en voeg met overige ingredienten toe.")
                .servings(6).ingredients(new ArrayList<>()).build();
        Ingredient ingredient1 = Ingredient.builder().name("spaghetti").quantity("500 gram").type("meel").recipe(recipe1).build();
        Ingredient ingredient2 = Ingredient.builder().name("eieren").quantity("10").type("eieren").recipe(recipe1).build();
        Ingredient ingredient3 = Ingredient.builder().name("spekblokjes").quantity("300 gram").type("vlees").recipe(recipe1).build();
        recipe1.getIngredients().addAll(Arrays.asList(ingredient1, ingredient2, ingredient3));

        recipe2 = Recipe.builder().recipeId(2)
                .name("spaghetti al olio")
                .instructions("Kook de spaghetti. Bak de knoflook in de olijfolie en voeg spaghetti toe wanneer gaar.")
                .servings(4).ingredients(new ArrayList<>()).build();
        Ingredient ingredient4 = Ingredient.builder().name("spaghetti").quantity("300 gram").type("meel").recipe(recipe2).build();
        Ingredient ingredient5 = Ingredient.builder().name("olijfolie").quantity("100 ml").type("olie").recipe(recipe2).build();
        Ingredient ingredient6 = Ingredient.builder().name("knoflook").quantity("1 bol").type("kruid").recipe(recipe2).build();
        recipe2.getIngredients().addAll(Arrays.asList(ingredient4, ingredient5, ingredient6));

        recipe3 = Recipe.builder().recipeId(3)
                .name("spaghetti al vongole")
                .instructions("Kook de spaghetti. Haal de vongole uit de schelpen en voeg toe met de wijn.")
                .servings(6).ingredients(new ArrayList<>()).build();
        Ingredient ingredient7 = Ingredient.builder().name("spaghetti").quantity("500 gram").type("meel").recipe(recipe3).build();
        Ingredient ingredient9 = Ingredient.builder().name("vongole").quantity("500 gram").type("schaaldieren").recipe(recipe3).build();
        Ingredient ingredient8 = Ingredient.builder().name("witte wijn").quantity("200 ml").type("drank").recipe(recipe3).build();
        recipe3.getIngredients().addAll(Arrays.asList(ingredient7, ingredient8, ingredient9));
    }
    @Test
    void givenRecipesWhenFilteredThenReturnsFilteredResults1(){
        // Given recipes from initRecipes()

        // When search recipes which include spaghetti and have 6 servings
        when(recipeRepository.findAllByIncludeIngredient("spaghetti")).thenReturn(Stream.of(recipe1, recipe2, recipe3).collect(Collectors.toSet()));
        when(recipeRepository.findAllByServings(6)).thenReturn(new LinkedHashSet<>(Arrays.asList(recipe1, recipe3)));

        String include = "spaghetti";
        String exclude = null;
        String instructions = null;
        Integer servings = 6;
        String vegetarian = null;

        Set<Recipe> recipes = recipeService.getRecipes(include, exclude, instructions, servings, vegetarian);

        // Then
        assertTrue(recipes.size() == 2);
        assertTrue(recipes.containsAll(Arrays.asList(recipe1, recipe3)));
    }

    @Test
    void givenRecipesWhenFilteredThenReturnsFilteredResults2(){
        // Given recipes from initRecipes()

        // When search recipes which exclude olijfolie and are vegetarian
        when(recipeRepository.findAllByExcludeIngredient("olijfolie")).thenReturn(Stream.of(recipe1, recipe3).collect(Collectors.toSet()));
        when(recipeRepository.findAllByIsVegetarian()).thenReturn(Stream.of(recipe2, recipe3).collect(Collectors.toSet()));

        String include = null;
        String exclude = "olijfolie";
        String instructions = null;
        Integer servings = null;
        String vegetarian = "yes";

        Set<Recipe> recipes = recipeService.getRecipes(include, exclude, instructions, servings, vegetarian);

        // Then
        assertTrue(recipes.size() == 1);
        assertTrue(recipes.containsAll(Arrays.asList(recipe3)));
    }

    @Test
    void givenRecipesWhenFilteredThenReturnsFilteredResults3(){
        // Given recipes from initRecipes()

        // When search recipes which include spaghetti and have 6 servings
        when(recipeRepository.findAllByInstructions("Klop")).thenReturn(new LinkedHashSet<>(Arrays.asList(recipe1)));
        when(recipeRepository.findAllByExcludeIngredient("eieren")).thenReturn(Stream.of(recipe2).collect(Collectors.toSet()));

        String include = null;
        String exclude = "eieren";
        String instructions = "Klop";
        Integer servings = null;
        String vegetarian = null;

        Set<Recipe> recipes = recipeService.getRecipes(include, exclude, instructions, servings, vegetarian);

        // Then no recipe is found.
        assertTrue(recipes.size() == 0);
    }

    // etc.

    @Test
    void givenIngredientIdWhenIngredientNotFoundThenEntityNotFoundExceptionIsThrown() {
        // Given a ingredient id
        var id = 10;
        // When the ingredient is not found in the ingredientRepository
        when(ingredientRepository.findById(10)).then(new ReturnsEmptyValues());
        // Then an exception is thrown
        var exception = assertThrows("Expected getIngredient to throw, but it didn't",
                EntityNotFoundException.class, () -> recipeService.getIngredient(id));

        var expectedMessage = ENTITY_NOT_FOUND + id;
        var actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void givenIngredientIdWhenIngredientFoundThenReturnsIngredient() {
        // Given a ingredient id
        var id = 10;
        // When the ingredient is found in the ingredientRepository
        when(ingredientRepository.findById(10)).thenReturn(Optional.of(new Ingredient()));
        // Then a ingredient is returned
        var ingredient = recipeService.getIngredient(id);
        assertNotNull(ingredient);
    }

    @Test
    void givenRecipeIdWhenRecipeNotFoundThenEntityNotFoundExceptionIsThrown() {
        // Given a recipe id
        var id = 10;
        // When the recipe is not found in the recipeRepository
        when(recipeRepository.findById(10)).then(new ReturnsEmptyValues());
        // Then an exception is thrown
        var exception = assertThrows("Expected getRecipe to throw, but it didn't",
                EntityNotFoundException.class, () -> recipeService.getRecipe(id));

        var expectedMessage = ENTITY_NOT_FOUND + id;
        var actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void givenRecipeIdWhenRecipeFoundThenReturnsRecipe() {
        // Given a recipe id
        var id = 10;
        // When the recipe is found in the ingredientRepository
        when(recipeRepository.findById(anyInt())).thenReturn(Optional.of(new Recipe()));
        // Then an recipe is returned
        var recipe = recipeService.getRecipe(id);
        assertNotNull(recipe);
    }

    @Test
    void whenIngredientsRequestReturnsListofIngredients() {
        // When search all ingredients in ingredientRepository
        when(ingredientRepository.findAll()).thenReturn(new ArrayList<Ingredient>());
        // A list of ingredients is returned
        var list = recipeService.getIngredients();
        assertNotNull(list);
    }

    @Test
    void whenRecipesRequestReturnsListOfRecipes() {
        // When search all recipes in recipeRepository
        when(recipeRepository.findAll()).thenReturn(new ArrayList<Recipe>());
        // A list of recipes is returned
        var list = recipeRepository.findAll();
        assertNotNull(list);
    }

    @Test
    void whenIngredientsByRecipeRequestReturnsListOfIngredients() {
        // When search all ingredients by recipe in ingredientRepository
        when(ingredientRepository.findIngredientsByRecipe(anyInt())).thenReturn(new ArrayList<Ingredient>());
        // A list of ingredients is returned
        var list = ingredientRepository.findIngredientsByRecipe(10);
        assertNotNull(list);
    }

    @Test
    void whenRecipeByNameRequestReturnsListOfRecipes() {
        // When search all recipes by part of name in ingredientRepository
        when(recipeRepository.findByName(anyString())).thenReturn(new ArrayList<>());
        // A list of recipes is returned
        var list = recipeRepository.findByName("name");
        assertNotNull(list);
    }

    // etc.
}

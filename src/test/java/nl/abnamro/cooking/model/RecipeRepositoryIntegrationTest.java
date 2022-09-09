package nl.abnamro.cooking.model;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;

import static org.junit.Assert.assertTrue;

@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace=AutoConfigureTestDatabase.Replace.NONE)
public class RecipeRepositoryIntegrationTest {
    @Autowired
    private RecipeRepository recipeRepository;

    @Test
    void givenRecipeWhenSavedThenFoundByName(){
        // Given recipe
        var recipe = Recipe.builder().name("pizza").build();
        //When an recipe is saved
        recipeRepository.save(recipe);
        // Then can be found by name
        assertTrue(recipeRepository.findByName("pizza").size() == 1);
    }

    @Test
    void givenRecipeWithIngredientsThenFoundByVegetarianAndNotByNonVegetarian(){
        // Given recipe
        var ingredient1 = Ingredient.builder().name("bouillon").type("specerijen").build();
        var ingredient2 = Ingredient.builder().name("prei").type("groente").build();

        var recipe = Recipe.builder().name("groentesoep")
                .ingredients(new ArrayList<Ingredient>(Arrays.asList(ingredient1, ingredient2))).build();
        //When an recipe is saved
        Recipe recipeSaved = recipeRepository.save(recipe);
        // Then can be found by name
        Set<Recipe> vegetarianRecipes = recipeRepository.findAllByIsVegetarian();
        assertTrue(vegetarianRecipes.contains(recipeSaved));
        Set<Recipe> nonVegetarianRecipes = recipeRepository.findAllByIsNonVegetarian();
        assertTrue(!nonVegetarianRecipes.contains(recipeSaved));
    }

    // etc.
}

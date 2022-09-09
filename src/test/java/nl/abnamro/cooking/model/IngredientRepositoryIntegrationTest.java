package nl.abnamro.cooking.model;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.Assert.assertTrue;

@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace=AutoConfigureTestDatabase.Replace.NONE)
public class IngredientRepositoryIntegrationTest {
    @Autowired
    private IngredientRepository ingredientRepository;

    @Test
    void givenIngredientWhenSavedThenFoundByName(){
        // Given ingredient
        var ingredientName = "mozzarella";
        var ingredient = Ingredient.builder().name(ingredientName).build();
        //When an recipe is saved
        var savedIngredient = ingredientRepository.save(ingredient);
        // Then can be found by name
        assertTrue(ingredientRepository.findByName(savedIngredient.getName()).size() == 1);
    }

    // etc.

}

package nl.abnamro.cooking.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.abnamro.cooking.model.Ingredient;
import nl.abnamro.cooking.model.Recipe;
import nl.abnamro.cooking.service.RecipeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@WebMvcTest(controllers = CookingRestController.class)
public class CookingRestControllerIntegrationTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private RecipeService recipeService;

    @Test
    void getRecipeById() throws Exception {
        Recipe recipe = Recipe.builder().recipeId(10).name("pizza").ingredients(new ArrayList<>()).build();
        when(recipeService.getRecipe(10)).thenReturn(recipe);

        mvc.perform(get("/api/cooking/v1/recipe/{id}", 10)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.Name").value("pizza"));
    }

    @Test
    void getIngredientById() throws Exception {
        Ingredient ingredient = Ingredient.builder().ingredientId(10).name("mozzarella").type("cheese").build();
        when(recipeService.getIngredient(10)).thenReturn(ingredient);

        mvc.perform(get("/api/cooking/v1/ingredient/{id}", 10)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.Name").value("mozzarella"))
                .andExpect(jsonPath("$.Type").value("cheese"));
    }

    // etc.

}

package nl.abnamro.cooking.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO for Recipes
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RecipeDTO {
    @JsonProperty("Id")
    private Integer id;
    @JsonProperty("Name")
    private String name;
    @JsonProperty("Genre")
    private String genre;
    @JsonProperty("Instructions")
    private String instructions;
    @JsonProperty("Servings")
    private Integer servings;
    @JsonProperty("Ingredients")
    private List<IngredientDTO> ingredients;
}

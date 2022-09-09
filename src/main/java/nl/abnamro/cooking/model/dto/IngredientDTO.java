package nl.abnamro.cooking.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for Ingredient
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class IngredientDTO {
    @JsonProperty("Id")
    private Integer id;
    @JsonProperty("Name")
    private String name;
    @JsonProperty("Quantity")
    private String quantity;
    @JsonProperty("Type")
    private String type;
}

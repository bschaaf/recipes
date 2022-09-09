package nl.abnamro.cooking.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.Set;

/**
 * Transfers a set of RecipeDTOs
 */
@Data
@XmlRootElement(name="recipes")
@AllArgsConstructor
@NoArgsConstructor
public class RecipesDTO {
    Set<RecipeDTO> recipes;
}

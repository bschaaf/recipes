package nl.abnamro.cooking.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.Set;


/**
 * Transfers a set of IngredientDTOs.
 */
@Data
@XmlRootElement(name="ingredients")
@AllArgsConstructor
@NoArgsConstructor
public class IngredientsDTO {
    Set<IngredientDTO> ingredient;
}

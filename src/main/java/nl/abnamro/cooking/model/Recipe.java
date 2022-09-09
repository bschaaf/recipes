package nl.abnamro.cooking.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;
import java.util.Objects;

/**
 * Recipe data class. One of the main classes of the data model. Is persisted as a database table.
 * Has a bidirectional relationship to the Ingredient data class.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement
@Entity
@Table(name = "recipes")
@Builder
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer recipeId;
    private String name;
    private String genre;
    @Length(max = 1024)
    private String instructions;
    private Integer servings;
    @OneToMany(mappedBy = "recipe", orphanRemoval = true, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Ingredient> ingredients;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Recipe recipe = (Recipe) o;
        return Objects.equals(recipeId, recipe.recipeId) && Objects.equals(name, recipe.name) && Objects.equals(genre, recipe.genre) && Objects.equals(instructions, recipe.instructions) && Objects.equals(servings, recipe.servings) && Objects.equals(ingredients, recipe.ingredients);
    }

    @Override
    public int hashCode() {
        return Objects.hash(recipeId, name, genre, instructions, servings, ingredients);
    }
}

package nl.abnamro.cooking.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Objects;

/**
 * Ingredient data class. One of the main classes of the data model. Is persisted as a database table.
 * Has a bidirectional relationship to the Recipe data class.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@XmlRootElement
@Entity
@Table(name = "ingredients", indexes = @Index(name="", columnList = "recipeId"))
@Builder
public class Ingredient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer ingredientId;
    private String name;
    private String quantity;
    private String type;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipeId")
    private Recipe recipe;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ingredient that = (Ingredient) o;
        return Objects.equals(ingredientId, that.ingredientId) && Objects.equals(name, that.name) && Objects.equals(quantity, that.quantity) && Objects.equals(type, that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ingredientId, name, quantity, type);
    }
}

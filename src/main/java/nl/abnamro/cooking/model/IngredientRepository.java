package nl.abnamro.cooking.model;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Data access layer for the Ingredient data class
 */
@Repository
public interface IngredientRepository extends JpaRepository<Ingredient, Integer> {
    @Query("select s from Ingredient s")
    List<Ingredient> findAll();
    @Query("select s from Ingredient s where s.ingredientId = :id")
    Optional<Ingredient> findById(Integer id);
    @Query("select s from Ingredient s where s.name = :name")
    List<Ingredient> findByName(String name);
    @Query("select s from Ingredient s where s.recipe.recipeId = :id")
    List<Ingredient> findIngredientsByRecipe(Integer id);
}

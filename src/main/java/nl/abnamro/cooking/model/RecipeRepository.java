package nl.abnamro.cooking.model;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Data access layer for the Recipe data class
 */
@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Integer> {
    @Query("select a from Recipe a")
    List<Recipe> findAll();
    @Query("select a from Recipe a where a.recipeId = :id")
    Optional<Recipe> findById(Integer id);

    @Query("select a from Recipe a where lower(a.name) like '%'||lower(:name)||'%'")
    List<Recipe> findByName(String name);

    @Query("select distinct a from Recipe a, Ingredient i where (a = i.recipe) and lower(i.name) like '%'||lower(:include)||'%'")
    Set<Recipe> findAllByIncludeIngredient(String include);

    @Query("select distinct a from Recipe a where a not in (select q from Recipe q, Ingredient i where (q = i.recipe) and lower(i.name) like '%'||lower(:exclude)||'%')")
    Set<Recipe> findAllByExcludeIngredient(String exclude);

    @Query("select distinct a from Recipe a where lower(a.instructions) like '%'||lower(:instructions)||'%'")
    Set<Recipe> findAllByInstructions(String instructions);

    @Query("select distinct a from Recipe a where a.servings = :servings")
    Set<Recipe> findAllByServings(Integer servings);

    @Query("select distinct a from Recipe a where not exists (select i from Ingredient i where (a = i.recipe) and (i.type = 'vlees' or i.type='gevogelte'))")
    Set<Recipe> findAllByIsVegetarian();

    @Query("select distinct a from Recipe a where exists (select i from Ingredient i where (a = i.recipe) and (i.type = 'vlees' or i.type='gevogelte'))")
    Set<Recipe> findAllByIsNonVegetarian();
}





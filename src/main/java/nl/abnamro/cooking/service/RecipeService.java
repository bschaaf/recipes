package nl.abnamro.cooking.service;

import lombok.RequiredArgsConstructor;
import nl.abnamro.cooking.controller.exceptions.EntityNotDeletedException;
import nl.abnamro.cooking.controller.exceptions.EntityNotFoundException;
import nl.abnamro.cooking.controller.mapping.MessageResponse;
import nl.abnamro.cooking.model.Ingredient;
import nl.abnamro.cooking.model.IngredientRepository;
import nl.abnamro.cooking.model.Recipe;
import nl.abnamro.cooking.model.RecipeRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static nl.abnamro.cooking.config.GlobalStringResources.*;

/**
 * The Recipe Service contains the business logic of the application and is
 * mostly called bij the REST api. Exceptions thrown are handled by
 * CookingControllerExceptionHandler for methods called from the REST api.
 *
 */
@Service
@RequiredArgsConstructor
public class RecipeService {
  private final IngredientRepository ingredientRepository;
  private final RecipeRepository recipeRepository;

  public Recipe createRecipe(Recipe recipe) {
    return recipeRepository.save(recipe);
  }

  public Ingredient createIngredient(Ingredient ingredient, Recipe recipe) {
    ingredient.setRecipe(recipe);
    return ingredientRepository.save(ingredient);
  }

  public Ingredient createIngredient(Ingredient ingredient, Integer recipeId) {
    Recipe recipe = recipeRepository.findById(recipeId).orElseThrow(() -> new EntityNotFoundException(ENTITY_NOT_FOUND + recipeId));
    return createIngredient(ingredient, recipe);
  }

  public Recipe getRecipe(Integer id) {
    return recipeRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(ENTITY_NOT_FOUND + id));
  }

  public Set<Recipe> getRecipes() {
    return recipeRepository.findAll().stream().collect(Collectors.toSet());
  }

  public List<Recipe> getRecipesByName(String name) {
    return recipeRepository.findByName(name);
  }

  public Set<Recipe> getRecipes(String include, String exclude, String instructions, Integer servings, String vegetarian) {
    boolean requestHasInclude = StringUtils.hasLength(include);
    boolean requestHasExclude = StringUtils.hasLength(exclude);
    boolean requestHasInstructions = StringUtils.hasLength(instructions);
    boolean requestHasServings = servings != null;
    boolean requestHasVegetarian = StringUtils.hasLength(vegetarian) && vegetarian.matches("yes|no");

    Set<Recipe> recipes = null;

    if(requestHasExclude) {
      recipes = initOrIntersect(recipes, recipeRepository.findAllByExcludeIngredient(exclude));
    }

    if(requestHasInclude) {
      recipes = initOrIntersect(recipes, recipeRepository.findAllByIncludeIngredient(include));
    }

    if(requestHasInstructions) {
      recipes = initOrIntersect(recipes, recipeRepository.findAllByInstructions(instructions));
    }

    if(requestHasServings) {
      recipes = initOrIntersect(recipes, recipeRepository.findAllByServings(servings));
    }

    if(requestHasVegetarian) {
      if("yes".equals(vegetarian)) {
        recipes = initOrIntersect(recipes, recipeRepository.findAllByIsVegetarian());
      } else {
        recipes = initOrIntersect(recipes, recipeRepository.findAllByIsNonVegetarian());
      }
    }

    return recipes;
  }

  private Set<Recipe> initOrIntersect(Set<Recipe> recipes, Set<Recipe> matchList) {
    return recipes == null ? matchList : intersect(recipes, matchList);
  }

  private Set<Recipe> intersect(Set<Recipe> recipes, Set<Recipe> matchSet) {
    return recipes.stream()
            .filter(recipe -> matchSet.contains(recipe))
            .collect(Collectors.toSet());
  }

  public Ingredient getIngredient(Integer id) {
    return ingredientRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(ENTITY_NOT_FOUND + id));
  }

  public List<Ingredient> getIngredientByName(String name) {
    return ingredientRepository.findByName(name);
  }

  public List<Ingredient> getIngredientsByRecipe(Integer id) {
    return ingredientRepository.findIngredientsByRecipe(id);
  }

  public Set<Ingredient> getIngredients() {
    return ingredientRepository.findAll().stream().collect(Collectors.toSet());
  }

  public Recipe updateRecipe(Recipe recipe, Integer id) {
    var existingRecipe = recipeRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException(ENTITY_NOT_FOUND + id));
    transferRecipeFields(recipe, existingRecipe);
    return recipeRepository.save(existingRecipe);
  }

  protected void transferRecipeFields(Recipe recipe, Recipe existingRecipe) {
    transferStringIfPresent(recipe.getName(), existingRecipe::setName);
    transferStringIfPresent(recipe.getGenre(), existingRecipe::setGenre);
    transferStringIfPresent(recipe.getInstructions(), existingRecipe::setInstructions);
    transferIntegerIfPresent(recipe.getServings(), existingRecipe::setServings);

    if(existingRecipe.getIngredients().size() > 0) {
      existingRecipe.getIngredients().forEach(ingredient -> deleteIngredient(ingredient.getIngredientId()));
      recipe.getIngredients().forEach(ingredient -> createIngredient(ingredient, recipe));
    }
  }

  protected void transferStringIfPresent(String field, Consumer<String> transfer) {
    if (StringUtils.hasLength(field))
      transfer.accept(field);
  }

  protected void transferIntegerIfPresent(Integer field, Consumer<Integer> transfer) {
    if (field != null)
      transfer.accept(field);
  }

  public Ingredient updateIngredient(Ingredient ingredient, Integer id) {
    var existingIngredient = ingredientRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException(ENTITY_NOT_FOUND + id));
    transferIngredientFields(ingredient, existingIngredient);
    return ingredientRepository.save(existingIngredient);
  }

  // Updates ingredient fields except recipe
  protected void transferIngredientFields(Ingredient ingredient, Ingredient existingIngredient) {
    transferStringIfPresent(ingredient.getName(), existingIngredient::setName);
    transferStringIfPresent(ingredient.getQuantity(), existingIngredient::setQuantity);
    transferStringIfPresent(ingredient.getType(), existingIngredient::setType);
  }

  public MessageResponse deleteIngredient(Integer id) {
    return deleteIdFromRepository(id, ingredientRepository);
  }

  public MessageResponse deleteRecipe(Integer id) {
    return deleteIdFromRepository(id, recipeRepository);
  }

  protected MessageResponse deleteIdFromRepository(Integer id, JpaRepository<?, Integer> repository) {
    repository.findById(id).ifPresentOrElse(o -> {
      try {
        repository.deleteById(id);
      } catch (DataIntegrityViolationException e) {
        throw new EntityNotDeletedException(CONSTRAINT_VIOLATION_DELETE);
      }
    }, () -> {
      throw new EntityNotDeletedException(ENTITY_NOT_FOUND + id);
    });

    return new MessageResponse(DELETE_SUCCESFUL + id);
  }

}

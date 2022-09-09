package nl.abnamro.cooking;

import lombok.extern.log4j.Log4j2;
import nl.abnamro.cooking.model.Recipe;
import nl.abnamro.cooking.model.dto.RecipeDTO;
import nl.abnamro.cooking.service.RecipeService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.web.reactive.function.client.WebClient;

import static nl.abnamro.cooking.controller.mapping.DTOMappers.map2Ingredient;
import static nl.abnamro.cooking.controller.mapping.DTOMappers.map2Recipe;

@Log4j2
@SpringBootApplication
public class CookingApplication {

	public static void main(String[] args) {
		SpringApplication.run(CookingApplication.class, args);
	}

	/**
	 * In the init method the recipes and corresponding ingredients are read and stored in the database.
	 * See application.properties for the relevant parameters.
	 */
	@Profile("!test")
	@Bean
	public CommandLineRunner init(@Value("${abnamro.baseUrl}") String recipeBaseUrl,
								  @Value("${abnamro.recipesFile}") String recipesFile,
								  RecipeService recipeService) {
		return args -> {
			var webClient = WebClient.builder().baseUrl(recipeBaseUrl).build();
			log.info("Reading recipe file..");
			webClient.get().uri('/' + recipesFile).retrieve().bodyToFlux(RecipeDTO.class)
					.subscribe(recipeDTO -> {
						Recipe recipe = recipeService.createRecipe(map2Recipe(recipeDTO));
						recipeDTO.getIngredients().forEach(ingredient -> recipeService.createIngredient(map2Ingredient(ingredient), recipe));
					});
		};
	}

}

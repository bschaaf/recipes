package nl.abnamro.cooking.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.validation.constraints.NotBlank;

/**
 * Provides de location of initial demo data
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "abnamro")
public class RecipesProperties {
    @NotBlank
    private String baseUrl;
    @NotBlank
    private String recipesFile;
}


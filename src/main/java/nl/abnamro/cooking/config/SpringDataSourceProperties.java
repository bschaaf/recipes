package nl.abnamro.cooking.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.validation.constraints.NotBlank;

/**
 * Provides de location and credentials of the database
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "spring.datasource")
public class SpringDataSourceProperties {
    @NotBlank
    private String url;
    @NotBlank
    private String username;
    @NotBlank
    private String password;
}


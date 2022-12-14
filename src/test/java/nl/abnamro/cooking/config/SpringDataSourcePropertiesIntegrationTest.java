package nl.abnamro.cooking.config;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ActiveProfiles("test")
@SpringBootTest
@TestPropertySource("classpath:application.properties")
public class SpringDataSourcePropertiesIntegrationTest {
 
    @Autowired
    private SpringDataSourceProperties springDataSourceProperties;
 
    @Test
    public void whenSimplePropertyQueriedThenReturnsPropertyValue() 
      throws Exception {
        assertTrue(StringUtils.isNotBlank(springDataSourceProperties.getUrl()));
        assertTrue(StringUtils.isNotBlank(springDataSourceProperties.getUsername()));
        assertTrue(StringUtils.isNotBlank(springDataSourceProperties.getPassword()));
    }  
}

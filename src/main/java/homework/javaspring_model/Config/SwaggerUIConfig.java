package homework.javaspring_model.Config;

import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.tags.Tag;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerUIConfig {

    @Bean
    public OpenApiCustomizer customOpenApiCustomizer() {
        return openApi -> {
            // Добавление глобальных параметров
            openApi.addTagsItem(new Tag().name("Users").description("User management operations"));

            // Настройка схемы
            openApi.getComponents()
                    .addSchemas("ErrorResponse", new Schema<Object>()
                            .addProperty("timestamp", new Schema<Object>().type("string"))
                            .addProperty("message", new Schema<Object>().type("string")));
        };
    }
}

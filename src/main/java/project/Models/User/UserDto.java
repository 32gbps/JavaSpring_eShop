package project.Models.User;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Value;

import java.util.UUID;

@Value
@Schema(description = "Пользователь")
public class UserDto {
    UUID userId;
    @Size(min = 8, max = 32)
    @NotBlank
    String username;

    @Schema(description = "&quot;Электронная почта&quot;, example = &quot;junior@example.com&quot;")
    @Email
    @NotBlank
    String email;

    @Schema(description = "&quot;Пароль должен содержать от 8 до 32 символов,&quot;" +
            "&quot;как минимум одну букву, одну цифру и один специальный символ&quot;")
    @Size(min = 8, max = 32)
    @NotBlank
    //@Pattern(regexp = "&quot;^(?=.*[A-Za-z])(?=.*\\\\d)(?=.*[@$!%*#?^&amp;])[A-Za-z\\\\d@$!%*#?^&amp;]{3,}$&quot;")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    String password;
}
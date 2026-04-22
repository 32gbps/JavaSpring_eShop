package homework.javaspring_model.Config;

import homework.javaspring_model.Services.CompanyService;
import homework.javaspring_model.Services.PersonService;
import jakarta.servlet.http.Cookie;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@AllArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final PersonService personService;
    private final CompanyService companyService;
    private static final Logger log = LoggerFactory.getLogger(SecurityConfig.class);

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login", "/register", "/css/**", "/js/**").permitAll()
                        .requestMatchers("/profile").hasAnyRole("USER","VENDOR", "MODERATOR", "ADMIN")
                        .requestMatchers("/adminPanel").hasAnyRole("ADMIN","MODERATOR")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .successHandler(
                                customAuthenticationSuccessHandler())
                        .failureUrl("/login?error")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logout")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                );

        return http.build();
    }
    @Bean
    public AuthenticationSuccessHandler customAuthenticationSuccessHandler() {
        return (request, response, authentication) -> {

            String username = authentication.getName();
            personService.findByUsername(username).ifPresent(x->{
                var id = x.getId();
                Cookie cookie = new Cookie("personId", id.toString());
                cookie.setPath("/");
                cookie.setMaxAge(30 * 24 * 60 * 60); // 30 дней
                cookie.setHttpOnly(false);
                //cookie.setSecure(true);
                response.addCookie(cookie);
            });
            companyService.findByUsername(username).ifPresent(x->{
                var id = x.getId();
                Cookie cookie = new Cookie("vendorId", id.toString());
                cookie.setPath("/");
                cookie.setMaxAge(30 * 24 * 60 * 60); // 30 дней
                cookie.setHttpOnly(false);
                //cookie.setSecure(true);
                response.addCookie(cookie);
            });
            response.sendRedirect("/");
        };
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
}
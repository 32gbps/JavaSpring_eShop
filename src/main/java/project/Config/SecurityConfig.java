package project.Config;

import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import project.Services.CustomerService;
import project.Services.UserService;
import project.Services.VendorService;
import jakarta.servlet.http.Cookie;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@AllArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final UserService userService;
    private final CustomerService customerService;
    private final VendorService vendorService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        //.requestMatchers("/login","/logout", "/register", "/css/**", "/js/**").permitAll()
                        .requestMatchers("/profile").hasAnyRole("CUSTOMER","VENDOR", "MODERATOR", "ADMIN")
                        .requestMatchers("/adminPanel", "/actuator").hasAnyRole("ADMIN","MODERATOR")
                        .anyRequest().permitAll()
//                        .anyRequest().authenticated()
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
                )
                .headers(headers -> headers
                        .frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin)
                );

        return http.build();
    }
    @Bean
    public AuthenticationSuccessHandler customAuthenticationSuccessHandler() {
        return (request, response, authentication) -> {

            var user = userService.findByUsername(authentication.getName());
            if(user.isPresent())
            {
                customerService.findByUsername(user.get().getUsername()).ifPresent(x->{
                    var id = x.id();
                    Cookie cookie = new Cookie("customerId", id.toString());
                    cookie.setPath("/");
                    cookie.setMaxAge(30 * 24 * 60 * 60); // 30 дней
                    cookie.setHttpOnly(false);
                    //cookie.setSecure(true);
                    response.addCookie(cookie);
                });
                vendorService.findByUsername(user.get().getUsername()).ifPresent(x->{
                    var id = x.id();
                    Cookie cookie = new Cookie("vendorId", id.toString());
                    cookie.setPath("/");
                    cookie.setMaxAge(30 * 24 * 60 * 60); // 30 дней
                    cookie.setHttpOnly(false);
                    //cookie.setSecure(true);
                    response.addCookie(cookie);
                });
            }

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
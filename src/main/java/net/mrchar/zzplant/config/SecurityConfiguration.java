package net.mrchar.zzplant.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@Import(CorsProperties.class)
@RequiredArgsConstructor
public class SecurityConfiguration {
    private final CorsProperties properties;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(this.properties.getOrigins());
        configuration.setAllowedMethods(this.properties.getMethods());
        configuration.setAllowedHeaders(this.properties.getHeaders());
        configuration.setAllowCredentials(this.properties.isWithCredentials());
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authorize -> {
            authorize.requestMatchers("/api/register").permitAll();
            authorize.requestMatchers("/api/login").permitAll();
            authorize.anyRequest().authenticated();
        });

        http.cors(withDefaults());
        http.csrf()
                .csrfTokenRepository(new CookieCsrfTokenRepository())
                .ignoringRequestMatchers("/api/login");

        http.exceptionHandling()
                .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED));

        http.formLogin()
                .usernameParameter("phoneNumber")
                .passwordParameter("password")
                .loginProcessingUrl("/api/login")
                .successForwardUrl("/api/login/success")
                .failureForwardUrl("/api/login/failure");

        http.logout().logoutUrl("/api/logout");
        return http.build();
    }
}

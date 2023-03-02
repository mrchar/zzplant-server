package net.mrchar.zzplant.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

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
        configuration.setAllowedHeaders(this.properties.getHeaders());
        configuration.setAllowedMethods(this.properties.getMethods());
        configuration.setAllowCredentials(this.properties.isWithCredentials());
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authorize -> {
            authorize.requestMatchers("/api/register").permitAll();
            authorize.anyRequest().permitAll();
        });

        http.formLogin().loginProcessingUrl("/api/login");
        http.logout().logoutUrl("/api/logout");
        return http.build();
    }
}

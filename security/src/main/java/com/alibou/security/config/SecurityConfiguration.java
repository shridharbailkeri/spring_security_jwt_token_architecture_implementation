package com.alibou.security.config;



import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static com.alibou.security.user.Role.ADMIN;
import static com.alibou.security.user.Role.MANAGER;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfiguration {


    private final JwtAuthenticationFilter jwtAuthenticationFilter;


    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf()
                .disable() // we r not using form login but rest thats why this is disabled
                .authorizeHttpRequests()
                .requestMatchers("/register", "/login")
                .permitAll()
                .requestMatchers("/admin").hasRole(ADMIN.name())
                .requestMatchers("/manager").hasAnyRole(ADMIN.name(), MANAGER.name())
                .anyRequest()
                .authenticated()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
                // why did i do this  .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
                // if you check the flow diagram which shows filter chain order spring security suggests
                // Authentication processing mechanisms
                // 1. UsernamePasswordAuthenticationFilter
                // 2. CasAuthenticationFilter
                // 3. BasicAuthenticationFilter
                // 4. etc
                // this is why u add custom jwt authentication before 1. UsernamePasswordAuthenticationFilter in filter chain
                // if we do somewhere else then we might affect the flow badly
                //
        return http.build();
    }

}

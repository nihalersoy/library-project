package com.tpe.security.config;

import com.tpe.security.jwt.AuthEntryPoint;
import com.tpe.security.jwt.AuthTokenFilter;
import com.tpe.security.service.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@RequiredArgsConstructor
public class WebSecurityConfig {

    private final UserDetailsServiceImpl userDetailsService;

    private final AuthEntryPoint unauthorizedHandler;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain (HttpSecurity http) throws Exception {

        http.cors().and().csrf()
                .disable()
                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler)
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests().antMatchers(AUTH_WHITE_LIST).permitAll()
                .anyRequest().authenticated();

        http.headers().frameOptions().sameOrigin();
        http.authenticationProvider(authenticationProvider());
        http.addFilterBefore(authJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public AuthTokenFilter authJwtTokenFilter(){
        return new AuthTokenFilter();
    }

    @Bean
    public PasswordEncoder encodePassword(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider(){

        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(encodePassword());
        return authenticationProvider;
    }

    @Bean
    public WebMvcConfigurer corsConfigurer(){

        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {

                registry.addMapping("/**") //tüm URL lere müsaade ediyoruz
                        .allowedOrigins("*") //butun kaynaklara izin veriyoruz, farklı sunucular veya domainler
                        .allowedHeaders("*") //Authorization, Content-Type
                        .allowedMethods("*"); //HTTP methodlarına izin veriyoruz(GET-POST-PUT-DELETE)

            }
        };

    }

    private static final String[] AUTH_WHITE_LIST = {
            "/",
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "index.html",
            "/css/**",
            "/images/**",
            "/js/**",
    };


}

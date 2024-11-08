package com.springboot.blog.config;



import com.springboot.blog.security.JwtAuthenticationEntryPoint;
import com.springboot.blog.security.JwtAuthenticationFilter;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
@SecurityScheme(
        name = "Bear Authentication",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "jwt",
        scheme = "Bearer"
)
public class SecurityConfig {

    private UserDetailsService userDetailsService;

    private JwtAuthenticationEntryPoint authenticationEntryPoint;

    private JwtAuthenticationFilter jwtAuthenticationFilter;


    public SecurityConfig(UserDetailsService userDetailsService,
                          JwtAuthenticationEntryPoint authenticationEntryPoint,
                          JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.userDetailsService = userDetailsService;
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }



    @Bean
    public static PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf((csrf)->csrf.disable()).authorizeHttpRequests(
                (authorize)->
//                        authorize.anyRequest().authenticated()
                        authorize.requestMatchers(HttpMethod.GET, "/api/**").permitAll()
                                .requestMatchers( "/api/auth/**").permitAll()
                                .requestMatchers("/swagger-ui/**").permitAll()
                                .requestMatchers("/v3/api-docs/**").permitAll()
                                .anyRequest().authenticated()
                ).exceptionHandling(exception-> exception
                .authenticationEntryPoint(authenticationEntryPoint)
                ).sessionManagement(session-> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class );

        return http.build();


    }

//    @Bean
//    public UserDetailsService userDetailsService(){
//        UserDetails dhananjay = User.builder()
//                .username("dhananjay")
//                .password(passwordEncoder().encode("dhananjay"))
//                .roles("USER")
//                .build();
//
//        UserDetails admin = User.builder()
//                .username("admin")
//                .password(passwordEncoder().encode("admin"))
//                .roles("ADMIN")
//                .build();
//
//        return new InMemoryUserDetailsManager(dhananjay, admin);
//    }


}

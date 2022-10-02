package backends.backendsite;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    private static final String[] AUTH_WHITELIST = {
            "/swagger-resources/**",
            "/swagger-ui.html",
            "/v3/api-docs",
            "/webjars/**",
            "/login", "/register"
    };


    @Bean
    public InMemoryUserDetailsManager userDetailService() {
        UserDetails user = User.withDefaultPasswordEncoder()
                .username("user@gmail.com")
                .password("password")
                .roles("USER")
                .build();
        return new InMemoryUserDetailsManager(user);
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity security) throws Exception {
        security
                .csrf().disable()
                .authorizeHttpRequests((auth) ->
                                auth
                                        .mvcMatchers(AUTH_WHITELIST).permitAll()
//                                .mvcMatchers("/ads/**", "/users/**").authenticated()
                )
                .cors().disable()
                .httpBasic(Customizer.withDefaults());
        return security.build();
    }
}
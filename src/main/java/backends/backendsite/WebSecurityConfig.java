package backends.backendsite;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

import static org.springframework.security.config.Customizer.withDefaults;

/**
 * Configuration class for web security
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    private static final String[] AUTH_WHITELIST = {
            "/swagger-resources/**",
            "/swagger-ui.html",
            "/v3/api-docs",
            "/webjars/**",
            "/login", "/register",
            "/ads/images/**"
    };

    @Autowired
    private DataSource dataSource;

//   filter of access to controller's methods
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity security) throws Exception {
        security
                .csrf().disable()
                .authorizeHttpRequests((auth) ->
                        auth
                                .mvcMatchers(AUTH_WHITELIST).permitAll()
                                .mvcMatchers("/ads/**", "/users/**").authenticated()

                )
                .cors().and()
                .httpBasic(withDefaults());

        return security.build();
    }


    @Bean
    public JdbcUserDetailsManager jdbcUserDetailsManager() {
        return new JdbcUserDetailsManager(dataSource);
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }



    //                .cors()
//                .and()
//                .csrf().disable()
////                .authorizeHttpRequests((auth) ->
////                        auth
////                                .mvcMatchers(AUTH_WHITELIST).permitAll()
////                                .mvcMatchers("/ads/**", "/users/**").authenticated()
////                )
//                .authorizeRequests()
//                .antMatchers("/login", "/register").permitAll()
//                .antMatchers(HttpMethod.GET, "/ads").permitAll()
//                .antMatchers("/users/*").authenticated()
//                .antMatchers("/ads**", "/users**").authenticated()
//                .mvcMatchers(AUTH_WHITELIST).permitAll()
//                .and()
//                .httpBasic();




}

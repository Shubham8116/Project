package com.example.technest.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.sql.DataSource;
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public JdbcUserDetailsManager userDetailsManager(DataSource dataSource)
    {
        JdbcUserDetailsManager manager = new JdbcUserDetailsManager(dataSource);

        // Custom query for loading users
        manager.setUsersByUsernameQuery(
                "SELECT email as username, password, enabled FROM users WHERE email = ?"
        );

        // Custom query for loading authorities (roles)
        manager.setAuthoritiesByUsernameQuery(
                "SELECT u.email as username, r.role_name AS authority FROM users_role ur " +
                        "JOIN users u ON ur.users_id = u.id " +
                        "JOIN role r ON ur.role_id = r.id WHERE u.email = ?"
        );
        return manager;
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception{
        httpSecurity.authorizeHttpRequests((request)->request
                        .requestMatchers("/css/*", "/js/", "/images/*").permitAll()
                        .requestMatchers("/").permitAll()
                        .requestMatchers("/signup").permitAll()
                        .requestMatchers("/register").permitAll()
                        .requestMatchers("/television" ,"/refrigerator","/washing-machine", "/geyser" ,"/purifier" ,"air-conditioner").permitAll()
                        .requestMatchers("/filter-television","/filter-refrigerator","/filter-washing-machine","/filter-geyser","/filter-air-conditioner","/filter-purifier").permitAll()
                        .requestMatchers("/contact-us").permitAll()
                        .requestMatchers(HttpMethod.GET,"/admin").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET,"/user").hasRole("USER")
                        .anyRequest().authenticated())
                .formLogin(form -> form.loginPage("/login")
                        .loginProcessingUrl("/authenticateTheUser")
                        .successHandler(customAuthenticationSuccessHandler())
                        .permitAll())  //to use custom login form
                .logout(logout->logout.permitAll()) // to add automatic functionality of logout
                .exceptionHandling(configurer->configurer.accessDeniedPage("/accessdenied"));  //to add functionality for exception(like access denied)

        httpSecurity.formLogin(Customizer.withDefaults());


        //use basic authentication
        //httpSecurity.httpBasic(Customizer.withDefaults());

        //disable cross site request frogery
        httpSecurity.csrf(csrf->csrf.disable());
        return httpSecurity.build();

    }

    @Bean
    public AuthenticationSuccessHandler customAuthenticationSuccessHandler() {
        return new CustomAuthenticationSuccessHandler();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}





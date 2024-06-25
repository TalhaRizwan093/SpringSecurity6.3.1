package com.tooster.security.config;

import com.tooster.security.service.UserInfoUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder){

        return new UserInfoUserDetailsService();

//        UserDetails admin = User.withUsername("talha")
//                .password(passwordEncoder.encode("1234"))
//                .roles("ADMIN")
//                .build();
//        UserDetails user = User.withUsername("john")
//                .password(passwordEncoder.encode("1234"))
//                .roles("USER")
//                .build();
//
//        return new InMemoryUserDetailsManager(admin,user);

    }

    @Bean
    @Order(1)
    public SecurityFilterChain securityFilterChainProduct(HttpSecurity http) throws Exception {
        return http.securityMatcher("/api/v1/product/**")
                .authorizeHttpRequests(authorize -> authorize
                                .requestMatchers("/api/v1/product/getProductById/{productId}").hasRole("USER")
                                .requestMatchers("/api/v1/product/getAllProducts").hasRole("ADMIN")
                        )
                .httpBasic(Customizer.withDefaults())
                .formLogin(Customizer.withDefaults())
                .build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain securityFilterChainUser(HttpSecurity http) throws Exception {
        return http.csrf(csrf -> csrf.ignoringRequestMatchers("/api/v1/user/**")).securityMatcher("/api/v1/user/**")
                .authorizeHttpRequests(authorize -> authorize.requestMatchers("/api/v1/user/**").permitAll()
                        .requestMatchers("/api/v1/user/register").permitAll()
                )
                .httpBasic(Customizer.withDefaults())
                .formLogin(Customizer.withDefaults())
                .build();
    }


    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }



}

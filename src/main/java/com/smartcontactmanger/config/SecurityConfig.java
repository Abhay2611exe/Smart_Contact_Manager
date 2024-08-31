package com.smartcontactmanger.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.smartcontactmanger.services.serviceimpl.SecurityCustomUserDetailService;




@Configuration
public class SecurityConfig {

    // user create and login using java code with in memeory service
    // @Bean
    // public UserDetailsService userDetailsService(){
        
    //     UserDetails user = User.withDefaultPasswordEncoder()
    //     .username("abhay")
    //     .password("abhay")
    //     .roles("ADMIN","USER")
    //     .build();

    //     var inMemeoryUserDetailManger = new InMemoryUserDetailsManager(user);
    //     return inMemeoryUserDetailManger;
    // }

    @Autowired
    private SecurityCustomUserDetailService userDetailsService;

    @Autowired
    private OAuthAuthenticationSuccessHandler authenticationSuccessHandler;

    @Autowired
    private AuthFailureHandler authFailureHandler;


    // Configuration of AuthenticationProvider for spring security
    @Bean
    public DaoAuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();

        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());

        return daoAuthenticationProvider; 
    }

    @Bean
    public SecurityFilterChain securtiyFilterChain(HttpSecurity httpSecurity) throws Exception{

        // Configuration
        httpSecurity.authorizeHttpRequests(authorize ->{
            // authorize.requestMatchers("/home","/register","/service").permitAll();
            authorize.requestMatchers("/user/**").authenticated();
            authorize.anyRequest().permitAll();
        });

        // httpSecurity.formLogin(Customizer.withDefaults());
        // Default login form
        httpSecurity.formLogin(formLogin ->{
            
            formLogin.loginPage("/login");
            formLogin.loginProcessingUrl("/authenticate");
            formLogin.successForwardUrl("/user/profile");
            // formLogin.failureForwardUrl("/login?error=true");

            formLogin.usernameParameter("email");
            formLogin.passwordParameter("password");

            formLogin.failureHandler(authFailureHandler);

            
        });

        // logout configured
        httpSecurity.csrf(AbstractHttpConfigurer::disable);
        httpSecurity.logout(formLogout ->{
            formLogout.logoutUrl("/logout");
            formLogout.logoutSuccessUrl("/login?logout=true");
        });

        // oauth configured
        httpSecurity.oauth2Login(oauth ->{
            oauth.loginPage("/login");
            oauth.successHandler(authenticationSuccessHandler);
        });

        return  httpSecurity.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}

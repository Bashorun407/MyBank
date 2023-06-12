package com.akinovapp.mybank.securityConfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.provisioning.InMemoryUserDetailsManager;
//import org.springframework.security.web.SecurityFilterChain;

@Configuration
//@EnableWebSecurity
public class BankSecurityConfiguration {
//    @Bean
//    public UserDetailsService userDetailsService(){
//        UserDetails firstUser = User.withUsername("olusheyi")
//                .password("1234")
//                .roles("ADMIN")
//                .build();
//
//        UserDetails secondUser = User.withUsername("user")
//                .password(passwordEncoder().encode("11112"))
//                .roles("USER")
//                .build();
//        return new InMemoryUserDetailsManager(firstUser, secondUser);
//    }
//
//    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
//      return  httpSecurity.csrf((csrf)->csrf.disable()
//                .authorizeHttpRequests(authorize -> {
//                    authorize
//                            .requestMatchers("/mybank/createUser").permitAll();
//                }));
//    }

   // @Bean
   // public PasswordEncoder passwordEncoder(){
//        return new BCryptPasswordEncoder();
//    }
   // SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
//        /*
//        disable csrf
//        authorize certain requests
//         */
//
//        httpSecurity.csrf().disable()
//                .authorizeHttpRequests()
//        return httpSecurity
//
//                .build();
//
// }


}

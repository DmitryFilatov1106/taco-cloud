package ru.fildv.tacocloud.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;
import ru.fildv.tacocloud.model.User;
import ru.fildv.tacocloud.repository.UserRepository;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
//     1 Variant
//    @Bean
//    public UserDetailsService userDetailsService(PasswordEncoder encoder) {
//        List<UserDetails> userDetails = new ArrayList<>();
//        userDetails.add(new User("ivan",
//                encoder.encode("123"),
//                Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"))));
//
//        userDetails.add(new User("petr",
//                encoder.encode("123"),
//                Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"))));
//
//        return new InMemoryUserDetailsManager(userDetails);
//    }

    //     2 Variant
    @Bean
    public UserDetailsService userDetailsService(
            final UserRepository userRepo) {
        return username -> {
            User user = userRepo.findByUsername(username);
            if (user != null) {
                return user;
            }
            throw new UsernameNotFoundException(
                    "User '" + username + "' not found");
        };
    }

    @Bean
    public SecurityFilterChain filterChain(
            final HttpSecurity http,
            final MvcRequestMatcher.Builder mvc)
            throws Exception {
        http
                .authorizeHttpRequests(
                        urlConfig -> urlConfig
                                .requestMatchers(
                                        mvc.pattern("/design"),
                                        mvc.pattern("/orders"))
                                .hasRole("USER")
                                .requestMatchers(
                                        mvc.pattern("/"),
                                        mvc.pattern("/**"))
                                .permitAll()
                )
                .formLogin(login -> login.loginPage("/login")
                        .defaultSuccessUrl("/", true))
                //.oauth2Login(login -> login.loginPage("/login"))
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login")
                        .deleteCookies("JSESSIONID"));
        return http.build();
    }

    @Scope("prototype")
    @Bean
    MvcRequestMatcher.Builder mvc(final HandlerMappingIntrospector handler) {
        return new MvcRequestMatcher.Builder(handler);
    }
}

package technikal.task.fishmarket.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public UserDetailsService users() {
        UserDetails admin = User.withUsername("admin")
                .password("{noop}admin")
                .roles("ADMIN")
                .build();

        UserDetails user = User.withUsername("user")
                .password("{noop}user")
                .roles("USER")
                .build();

        return new InMemoryUserDetailsManager(admin, user);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())

                .authorizeHttpRequests(auth -> auth

                        .requestMatchers("/login", "/error",
                                "/images/**", "/css/**", "/js/**").permitAll()
                        .requestMatchers("/fish/create/**", "/fish/delete/**").hasRole("ADMIN")

                        .requestMatchers("/fish/**", "/").authenticated()

                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .defaultSuccessUrl("/fish", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .permitAll()
                );

        return http.build();
    }
}

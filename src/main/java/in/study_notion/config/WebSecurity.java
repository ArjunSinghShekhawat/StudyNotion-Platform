package in.study_notion.config;
import in.study_notion.filters.JwtValidation;
import in.study_notion.services.implementation.CustomeUserDetailsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@EnableWebSecurity
@Configuration
@Slf4j
public class WebSecurity {

    @Autowired
    private CustomeUserDetailsService customeUserDetailsService;

    @Autowired
    private JwtValidation jwtValidation;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        try{
            return httpSecurity.sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                    .csrf(AbstractHttpConfigurer::disable)
                    .authorizeHttpRequests(auth->
                            auth.requestMatchers("/auth/**","/public/**","/reset-password/**","/contact-us/**").permitAll()
                                    .requestMatchers("/student/**").hasRole("STUDENT")
                                    .requestMatchers("/admin/**").hasRole("ADMIN")
                                    .requestMatchers("/instructor/**").hasRole("INSTRUCTOR")
                                    .anyRequest().authenticated())
                    .addFilterBefore(jwtValidation,UsernamePasswordAuthenticationFilter.class)
                    .build();

        }catch(Exception e) {
            log.error("Exception occurred while check web security {} ", e.getMessage());
            throw new Exception("Exception occurred while check web security");
        }
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(){
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(customeUserDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}

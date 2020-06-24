package com.example.demo.Config;

import com.example.demo.security.JwtAuthenticationFilter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@AllArgsConstructor
@EnableWebSecurity                                                             //main annotation to enable web security module
public class SecurityConfig extends WebSecurityConfigurerAdapter {               //WebSecurityConfigurerAdapter - provides default security config which we can override and customize

    private final UserDetailsService userDetailsService;
 //   private final JwtAuthenticationFilter jwtA;

    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception{
        return super.authenticationManagerBean();
    }


    @Override
    public void configure(HttpSecurity httpSecurity) throws Exception {
            httpSecurity.csrf().disable().                                        // "csrf" csrf attacks occur mainly when there are sessions and we are using cookies
                    authorizeRequests().antMatchers("/api/auth/**").permitAll()   // to authenticate session info but rest api are stateless and we're using json web tokens
                  //  .antMatchers(HttpMethod.GET,"/api/subreddit").permitAll()
                    .anyRequest().authenticated();
      //      httpSecurity.addFilterBefore(jwtA, UsernamePasswordAuthenticationFilter.class);         // To make springSecurity know about our JwtAuthenticationFilter class
    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();                                        // Bean of PasswordEncoder, when we'll autowire it, we'll get an instance of BCryptPasswordEncoder
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }
}

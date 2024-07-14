package com.example.jangkau.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

@Configuration
@EnableResourceServer
@EnableGlobalMethodSecurity(securedEnabled = true) //secure definition
public class Oauth2ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

    private static final String[] WHITE_LIST_URL = {
            "/error**",
            "/auth/**",
            "/auth",
            "/oauth/authorize**",
            "/api-docs/**",
            "/swagger-ui/**",
            "/api-docs/**",
            "/swagger-resources/**",
            "/users/**",
            "/users"
    };

    /**
     * Manage resource server.
     */
    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        super.configure(resources);
    }

    /**
     * Manage endpoints.
     */
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.cors()
                .and()
                .csrf()
                .disable()
                .antMatcher("/**")
                .authorizeRequests()
                .antMatchers(WHITE_LIST_URL)
                .permitAll()
                .antMatchers(HttpMethod.PUT, "/users").hasAnyAuthority("ROLE_USER")
                .antMatchers(HttpMethod.POST, "/users").hasAnyAuthority("ROLE_ADMIN")
                .antMatchers(HttpMethod.DELETE, "/users").hasAnyAuthority("ROLE_ADMIN")
                .antMatchers(HttpMethod.GET, "/users").hasAnyAuthority("ROLE_ADMIN")
                .and()
                .authorizeRequests()
                .anyRequest()
                .authenticated()
                .and()
                .formLogin()
                .permitAll();
    }
}

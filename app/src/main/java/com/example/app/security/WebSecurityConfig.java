package com.example.app.security;

import com.example.app.service.Impls.UserServiceImpl;
import com.example.app.filter.JWTAuthenticationFilter;
import com.example.app.filter.JWTAuthorizationFilter;
import com.example.app.utils.JWTHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.NegatedRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import static com.example.app.controller.PostController.ID;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final String LOGIN = "/login";
    private static final String BASE_URL_FOR_USER_CONTROLLER = "/v1/users";
    private static final String BASE_URL_FOR_POST_CONTROLLER = "/v1/posts";
    private static final String BASE_URL_FOR_WELCOME_CONTROLLER = "/";

    private final RequestMatcher publicUrls;
    private final RequestMatcher loginRequest;
    private final JWTHelper jwtHelper;

    public WebSecurityConfig(@Value("${base-url}") final String baseUrl,
                             final JWTHelper jwtHelper) {
        this.loginRequest = new AntPathRequestMatcher(baseUrl + LOGIN, POST.toString());
        this.publicUrls = new OrRequestMatcher(loginRequest,
                new AntPathRequestMatcher(BASE_URL_FOR_USER_CONTROLLER, POST.toString()),
                new AntPathRequestMatcher(BASE_URL_FOR_POST_CONTROLLER, GET.toString()),
                new AntPathRequestMatcher(BASE_URL_FOR_POST_CONTROLLER + ID, GET.toString()),
                new AntPathRequestMatcher(BASE_URL_FOR_WELCOME_CONTROLLER, GET.toString()),
                new NegatedRequestMatcher(new AntPathRequestMatcher(baseUrl + "/**"))
        );
        this.jwtHelper = jwtHelper;
    }


    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(passwordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        final JWTAuthenticationFilter authenticationFilter = new JWTAuthenticationFilter(
                authenticationManagerBean(),
                loginRequest,
                jwtHelper
        );

        final JWTAuthorizationFilter authorizationFilter = new JWTAuthorizationFilter(
                publicUrls,
                jwtHelper
        );

        http.csrf().disable()
                .headers().frameOptions().disable()
                .and()
                .authorizeRequests()
                .requestMatchers(publicUrls).permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilter(authenticationFilter)
                .addFilterBefore(authorizationFilter, UsernamePasswordAuthenticationFilter.class)
                .sessionManagement().disable()
                .formLogin().disable()
                .httpBasic().disable()
                .logout().disable();
    }
}

package com.example.app.filter;

import com.example.app.dto.UserDto;
import com.example.app.utils.JWTHelper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.stream.Collectors;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final JWTHelper jwtHelper;

    private static final ObjectMapper MAPPER = new ObjectMapper();


    public JWTAuthenticationFilter(final AuthenticationManager authenticationManager,
                                   final RequestMatcher loginRequest,
                                   final JWTHelper jwtHelper) {
        super(authenticationManager);
        super.setRequiresAuthenticationRequestMatcher(loginRequest);
        this.jwtHelper = jwtHelper;
    }

    @Override
    public Authentication attemptAuthentication(final HttpServletRequest request,
                                                final HttpServletResponse response) throws AuthenticationException {
        final UserDto userDto = getLoginData(request);
        final UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(
                userDto.getUsername(),
                userDto.getPassword()
        );
        setDetails(request, authRequest);
        return getAuthenticationManager().authenticate(authRequest);
    }

    private UserDto getLoginData(final HttpServletRequest request) throws AuthenticationException {
        try {
            final String json = request.getReader()
                    .lines()
                    .collect(Collectors.joining());
            Map<String, String> mapRequest =  MAPPER.readValue(json, new TypeReference<>() {
            });
            return new UserDto(mapRequest.get("username"), mapRequest.get("password"));
        } catch (IOException e) {
            throw new BadCredentialsException("Can't extract login data from request");
        }
    }

    protected void successfulAuthentication(final HttpServletRequest request,
                                            final HttpServletResponse response,
                                            final FilterChain chain,
                                            final Authentication authResult) throws IOException {
        final UserDetails user = (UserDetails) authResult.getPrincipal();
        final String token = jwtHelper.expiring(Map.of("username", user.getUsername()));

        response.getWriter().print(token);
    }
}

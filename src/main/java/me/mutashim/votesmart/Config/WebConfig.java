package me.mutashim.votesmart.Config;

import jakarta.servlet.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.IOException;

@Configuration
public class WebConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("http://127.0.0.1:3000")
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*")
                        .allowCredentials(true);  // Ensure cookies are included
            }
        };
    }

    @Bean
    public Filter cookieFilter() {
        return new Filter() {
            @Override
            public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
                    throws IOException, ServletException {

                // Cast ServletRequest to HttpServletRequest to access session
                HttpServletRequest httpRequest = (HttpServletRequest) request;
                HttpServletResponse httpResponse = (HttpServletResponse) response;

                // Get session ID from HttpServletRequest
                String sessionId = httpRequest.getSession().getId();

                // Create a new cookie for JSESSIONID
                Cookie cookie = new Cookie("JSESSIONID", sessionId);
                cookie.setPath("/");
                cookie.setHttpOnly(true);
                cookie.setSecure(false);  // Set to false for local development
                cookie.setMaxAge(3600);   // Set session expiry (1 hour)

                // Manually set SameSite attribute in cookie header
                httpResponse.addHeader("Set-Cookie", "JSESSIONID=" + sessionId + "; Path=/; HttpOnly; Max-Age=3600; SameSite=None; Secure");

                // Proceed with the filter chain
                chain.doFilter(request, response);
            }
        };
    }
}

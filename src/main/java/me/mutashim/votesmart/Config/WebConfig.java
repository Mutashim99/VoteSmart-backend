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
                        .allowCredentials(true);
            }
        };
    }

    @Bean
    public Filter cookieFilter() {
        return new Filter() {
            @Override
            public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
                    throws IOException, ServletException {


                HttpServletRequest httpRequest = (HttpServletRequest) request;
                HttpServletResponse httpResponse = (HttpServletResponse) response;


                String sessionId = httpRequest.getSession().getId();


                Cookie cookie = new Cookie("JSESSIONID", sessionId);
                cookie.setPath("/");
                cookie.setHttpOnly(true);
                cookie.setSecure(false);
                cookie.setMaxAge(3600);


                httpResponse.addHeader("Set-Cookie", "JSESSIONID=" + sessionId + "; Path=/; HttpOnly; Max-Age=3600; SameSite=None; Secure");


                chain.doFilter(request, response);
            }
        };
    }
}

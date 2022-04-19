package spring.labserver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {
    
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        // 내 서버가 응답을 할 때 json을 자바스크립트에서 처리할 수 있게 할지를 설정하는 것
        // false인 경우 JS로 응답을 보내주지 않음
        config.setAllowCredentials(true);
        // 모든 IP에 응답을 허용 함
        config.addAllowedOrigin("*");
        // 모든 HEADER에 응답을 허용 함
        config.addAllowedHeader("*");
        // 모든 POST, GET, PUT, DELETE, PATCH 요청을 허용 함
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/api/**", config);
        return new CorsFilter(source);
    }
}

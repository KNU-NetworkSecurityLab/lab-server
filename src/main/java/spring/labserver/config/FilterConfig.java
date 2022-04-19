package spring.labserver.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import spring.labserver.filters.JwtAuthenticationFilter;
import spring.labserver.filters.JwtAuthorizationFilter;

@Configuration
public class FilterConfig {
    // SecurityConfig에 있는 필터 먼저 실행된후 실행되는 필터들
    @Bean
    public FilterRegistrationBean<JwtAuthenticationFilter> filter1() {
        FilterRegistrationBean<JwtAuthenticationFilter> bean = new FilterRegistrationBean<>(new JwtAuthenticationFilter());
        bean.addUrlPatterns("/*");
        // 낮은 번호가 필터 중에서 가장 먼저 실행됨
        bean.setOrder(0);
        return bean;
    }

    @Bean
    public FilterRegistrationBean<JwtAuthorizationFilter> filter2() {
        FilterRegistrationBean<JwtAuthorizationFilter> bean = new FilterRegistrationBean<>(new JwtAuthorizationFilter());
        bean.addUrlPatterns("/*");
        // 낮은 번호가 필터 중에서 가장 먼저 실행됨
        bean.setOrder(1);
        return bean;
    }
}

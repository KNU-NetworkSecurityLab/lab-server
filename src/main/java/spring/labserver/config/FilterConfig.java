package spring.labserver.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import spring.labserver.filters.MyFilter1;
import spring.labserver.filters.MyFilter2;

@Configuration
public class FilterConfig {
    // SecurityConfig에 있는 필터 먼저 실행된후 실행되는 필터들
    @Bean
    public FilterRegistrationBean<MyFilter1> filter1() {
        FilterRegistrationBean<MyFilter1> bean = new FilterRegistrationBean<>(new MyFilter1());
        bean.addUrlPatterns("/*");
        // 낮은 번호가 필터 중에서 가장 먼저 실행됨
        bean.setOrder(0);
        return bean;
    }

    @Bean
    public FilterRegistrationBean<MyFilter2> filter2() {
        FilterRegistrationBean<MyFilter2> bean = new FilterRegistrationBean<>(new MyFilter2());
        bean.addUrlPatterns("/*");
        // 낮은 번호가 필터 중에서 가장 먼저 실행됨
        bean.setOrder(1);
        return bean;
    }
}

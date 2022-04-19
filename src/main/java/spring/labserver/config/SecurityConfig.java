package spring.labserver.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;
import org.springframework.web.filter.CorsFilter;

import lombok.RequiredArgsConstructor;
import spring.labserver.filters.JwtAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter{

    private final CorsFilter corsFilter;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // http.addFilterBefore(new JwtAuthenticationFilter(), SecurityContextPersistenceFilter.class);
        // csrf disable
        http.csrf().disable();
        // 세션을 만드는 방식을 사용하지 않겠다는 의미, STATELESS서버(세션 없는 서버)로 만들겠다.
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        // 모든 요청은 corsFilter를 거침
        .addFilter(corsFilter)
        // form 태그로 로그인을 사용하지 않음
        .formLogin().disable()
        // 기본적인 http 로그인 방식을 사용하지 않음
        // httpBasic는 헤더에 ID와 PW를 담아서 보내는 방식
        // JWT는 헤더에 ID와 PW에 해당하는 정보를 포함한 토큰을 담음
        .httpBasic().disable()
        .authorizeRequests()
        // user api에는 user, manager, admin 접근가능
        .antMatchers("/api/v1/user/**")
        .access("hasRole('ROLE_USER') or hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
        // manager api에는 manager, admin만 접근가능
        .antMatchers("/api/v1/manager/**")
        .access("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
        // admin api에는 admin만 접근가능
        .antMatchers("/api/v1/admin/**")
        .access("hasRole('ROLE_ADMIN')")
        // 그외 API
        .anyRequest().permitAll();
    }
}

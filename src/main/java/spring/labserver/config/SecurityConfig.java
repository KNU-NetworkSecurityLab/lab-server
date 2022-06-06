package spring.labserver.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

import spring.labserver.config.jwt.JwtAuthenticationFilter;
import spring.labserver.config.jwt.JwtAuthorizationFilter;
import spring.labserver.services.UserService;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{

    @Autowired
    private CorsConfig corsConfig;

    @Autowired
    private UserService userService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 모든 요청은 corsFilter를 거침
        http.addFilter(corsConfig.corsFilter())
        // csrf disable
        .csrf().disable()
        // 세션을 만드는 방식을 사용하지 않겠다는 의미, STATELESS서버(세션 없는 서버)로 만들겠다.
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        // form 태그로 로그인을 사용하지 않음
        .formLogin().disable()
        // 기본적인 http 로그인 방식을 사용하지 않음
        // httpBasic는 헤더에 ID와 PW를 담아서 보내는 방식
        // JWT는 헤더에 ID와 PW에 해당하는 정보를 포함한 토큰을 담음
        .httpBasic().disable()
        // AuthenticationManager을 던져야 함
        // 인증 후에 인가된 사용자의 권한을 확인하고 인증을 완료하는 방식
        .addFilter(new JwtAuthenticationFilter(authenticationManager()))
        .addFilter(new JwtAuthorizationFilter(authenticationManager(), userService))
        // // JwtAuthenticationFilter 앞에 위치시키는 필터
        // // 인증 실패시 예외를 던지는 필터
        // .addFilterBefore(new JwtExceptionFilter(), JwtAuthenticationFilter.class)
        .authorizeRequests()
        // user api에는 user, admin 접근가능
        .antMatchers("/api/v1/user/**")
        .access("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
        // admin api에는 admin만 접근가능
        .antMatchers("/api/v1/admin/**")
        .access("hasRole('ROLE_ADMIN')")
        // 그외 API
        // .antMatchers("/api/v1/**").permitAll()
        .anyRequest().permitAll();
    }
}

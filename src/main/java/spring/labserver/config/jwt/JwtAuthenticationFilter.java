package spring.labserver.config.jwt;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.RequiredArgsConstructor;

// 스프링 시큐리티에서 UsernamePasswordAuthenticationFilter가 있음
// /login 요청해서 name, password 정송하면 (POST) 해당 필터가 동작 함
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter{
    
    // 받은 AuthenticationManager으로 로그인 시도하면 됨
    private final AuthenticationManager authenticationManager;

    // /login api 요청을 하면 로그인 시도를 위해서 실행되는 함수
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {

        // id와 password 받아서 정상인지 로그인 시도를 해보면 됨
        // authenticationManager로 로그인 시도를 하면 PrincipalDetailsService가 실행 됨
        // PrincipalDetailsService을 세션에 담고(세션에 담는 이유는 권한 관리를 위해서임)
        // JWT토큰을 만들어서 client에 응답해주면 됨
        System.out.println("JwtAuthenticationFilter : 로그인 시도중");
        return super.attemptAuthentication(request, response);
    }
}

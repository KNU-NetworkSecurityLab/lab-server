package spring.labserver.config.jwt;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import spring.labserver.config.details.PrincipalDetails;
import spring.labserver.domain.user.User;
import spring.labserver.domain.user.UserRepository;

// 인가

// 시큐리티의 필터 중 BasicAuthenticationFilter가 있음
// 권한이나 인증이 필요한 특정 주소를 요청했을 때 위 필터를 무조건 거치게 되어있음.
// 만약 권한이나 인증이 필요한 주소가 아니라면 이 필터를 거치지 않음
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private UserRepository userRepository;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, UserRepository userRepository) {
        super(authenticationManager);
        this.userRepository = userRepository;
    }
    
    // 인증이나 권한이 필요한 주소요청이 있을 때 해당 필터를 타게 됨
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        String jwtHeader = request.getHeader(JwtProperties.HEADER_STRING);
        // header가 있는지 확인 없으면 리턴
        if(jwtHeader == null || !jwtHeader.startsWith(JwtProperties.TOKEN_PREFIX)) {
            chain.doFilter(request, response);
            return;
        }
        System.out.println("header = " + jwtHeader);
        // JWT 토큰을 검증해서 정상적인 사용자인지 확인
        String jwtToken = request.getHeader(JwtProperties.HEADER_STRING).replace(JwtProperties.TOKEN_PREFIX, "");
        String userId = JWT.require(Algorithm.HMAC512(JwtProperties.SECRET)).build().verify(jwtToken).getClaim("userId").asString();

        // 서명이 정상적으로 됨
        if(userId != null) {
            User userEntity = userRepository.findByUserId(userId);
            PrincipalDetails principalDetails = new PrincipalDetails(userEntity);

            // jwt토큰 서명을 통해서 서명이 정상인걸 확인하면, Authentication 객체를 만들어 줌
            Authentication authentication = new UsernamePasswordAuthenticationToken(principalDetails, null, principalDetails.getAuthorities());

            // 강제로 시큐리티 세션이 접근하여 Authentication 객체를 저장
            SecurityContextHolder.getContext().setAuthentication(authentication);

        }
        chain.doFilter(request, response);
    }
}

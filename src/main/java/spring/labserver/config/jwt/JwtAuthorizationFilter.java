package spring.labserver.config.jwt;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import spring.labserver.config.details.PrincipalDetails;
import spring.labserver.domain.user.User;
import spring.labserver.services.UserService;

// 인가

// 시큐리티의 필터 중 BasicAuthenticationFilter가 있음
// 권한이나 인증이 필요한 특정 주소를 요청했을 때 위 필터를 무조건 거치게 되어있음.
// 만약 권한이나 인증이 필요한 주소가 아니라면 이 필터를 거치지 않음
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private UserService userService;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, UserService userService) {
        super(authenticationManager);
        this.userService = userService;
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
            // System.out.println("header = " + jwtHeader);
            
            try {

                // JWT 토큰을 검증해서 정상적인 사용자인지 확인
                String jwtToken = request.getHeader(JwtProperties.HEADER_STRING).replace(JwtProperties.TOKEN_PREFIX, "");
                String userId = JWT.require(Algorithm.HMAC512(JwtProperties.SECRET)).build().verify(jwtToken).getClaim("userId").asString();

                // 서명이 정상적으로 됨
                if(userId != null) {
                    User userEntity = userService.findByUserId(userId);
                    PrincipalDetails principalDetails = new PrincipalDetails(userEntity);

                    // JWT 토큰 서명을 통해서 서명이 정상인걸 확인하면, Authentication 객체를 만들어 줌
                    Authentication authentication = new UsernamePasswordAuthenticationToken(principalDetails, null, principalDetails.getAuthorities());

                    // JWT 토큰 갱신
                    // Hash 암호 방식
                    String refreshToken = JWT.create()
                    .withSubject("nsl토큰")
                    // 만료시간
                    .withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.EXPIRATION_TIME))
                    // 비공개 클래임으로 키-밸류 값 
                    .withClaim("id", principalDetails.getUser().getId())
                    .withClaim("userId", principalDetails.getUser().getUserId())
                    .sign(Algorithm.HMAC512(JwtProperties.SECRET));
                    response.addHeader(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX + refreshToken);

                    // 강제로 시큐리티 세션이 접근하여 Authentication 객체를 저장
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
                
            // 토큰이 만료되었을 때
            } catch(TokenExpiredException e) {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);

                // JSON으로 만들기 위해 Map으로 데이터 저장 
                Map<String, Object> errorMessage = new HashMap<>();               
                // errorMessage.put("StatusCode", HttpStatus.UNAUTHORIZED.value());
                errorMessage.put("msg", "Token Expired");

                // JSON 형태로 메시지 생성
                new ObjectMapper().writeValue(response.getOutputStream(), errorMessage);    
            
            // 토큰이 유효하지 않(틀렸)을 때
            } catch(SignatureVerificationException e) {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);

                // JSON으로 만들기 위해 Map으로 데이터 저장 
                Map<String, Object> errorMessage = new HashMap<>();               
                // errorMessage.put("StatusCode", HttpStatus.UNAUTHORIZED.value());
                errorMessage.put("msg", "Token Wrong");

                // JSON 형태로 메시지 생성
                new ObjectMapper().writeValue(response.getOutputStream(), errorMessage);    
            
            // 토큰에 대한 ID가 존재하지 않을 때
            } catch(NullPointerException e) {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);

                // JSON으로 만들기 위해 Map으로 데이터 저장 
                Map<String, Object> errorMessage = new HashMap<>();               
                // errorMessage.put("StatusCode", HttpStatus.UNAUTHORIZED.value());
                errorMessage.put("msg", "Authorization Failed");

                // JSON 형태로 메시지 생성
                new ObjectMapper().writeValue(response.getOutputStream(), errorMessage);    

            // 그 외 예외처리            
            } catch(BadCredentialsException | JWTVerificationException e) {
                // 인증이 실패했을 때 이런 예외가 발생하면 이런 예외를 던짐
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);

                // JSON으로 만들기 위해 Map으로 데이터 저장 
                Map<String, Object> errorMessage = new HashMap<>();               
                // errorMessage.put("StatusCode", HttpStatus.UNAUTHORIZED.value());
                errorMessage.put("msg", "Authorization Failed");

                // JSON 형태로 메시지 생성
                new ObjectMapper().writeValue(response.getOutputStream(), errorMessage);
            }

        chain.doFilter(request, response);    
    }
}

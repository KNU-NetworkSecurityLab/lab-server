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
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.RequiredArgsConstructor;
import spring.labserver.config.details.PrincipalDetails;
import spring.labserver.domain.user.User;

// 인증

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

        System.out.println("JwtAuthenticationFilter : 로그인 시도중");

        // 사용자의 id와 password 받기
        try {
            ObjectMapper om = new ObjectMapper();
            User user = om.readValue(request.getInputStream(), User.class);
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUserId(), user.getPassword());

            // PrincipalDetailsService의 loadUserByUsername() 함수가 실행됨
            // 실행결과가 정상이면 authentication이 리턴됨
            // DB에 있는 userId와 password가 일치한다
            Authentication authentication = authenticationManager.authenticate(authenticationToken);

            // authentication 객체를 session 영역에 저장하고 그걸 return 해주면 됨
            // 리턴하는 이유는 권한 관리를 security가 대신 해주기 때문에 편함
            // 굳이 JWT 토큰을 사용하면서 세션을 만들 이유가 없음 단지 권한 처리 때문에 session을 넣어 줌

            return authentication;

        // 로그인 하려는 ID가 없을 때
        } catch(NullPointerException e) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);

            // JSON으로 만들기 위해 Map으로 데이터 저장 
            Map<String, Object> errorMessage = new HashMap<>();               
            errorMessage.put("msg", "Authorization Failed");

            // JSON 형태로 메시지 생성
            try {
                new ObjectMapper().writeValue(response.getOutputStream(), errorMessage);
            } catch (IOException e1) {
                e1.printStackTrace();
            }    

        // 자격 증명 실패시         
        } catch(BadCredentialsException e) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);

            // JSON으로 만들기 위해 Map으로 데이터 저장 
            Map<String, Object> errorMessage = new HashMap<>();               
            errorMessage.put("msg", "Authorization Failed");

            // JSON 형태로 메시지 생성
            try {
                new ObjectMapper().writeValue(response.getOutputStream(), errorMessage);
            } catch (IOException e1) {
                e1.printStackTrace();
            }    

        } catch(IOException e) {
            e.printStackTrace();
        }

        // 오류시 null 리턴
        return null; 
    }  

    // attemptAuthentication 실행 후 인증이 정상적으로 되었으면 아래 successfulAuthentication가 실행 됨
    // 여기 함수에서 JWT토큰을 만들어서 request 요청한 사용자에게 response 해주면 됨
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
            Authentication authResult) throws IOException, ServletException {
        System.out.println("successfulAuthentication 실행됨 : 인증 완료");
        PrincipalDetails principalDetails = (PrincipalDetails) authResult.getPrincipal();

        // Hash 암호 방식
        String jwtToken = JWT.create()
            .withSubject("nsl토큰")
            // 만료시간
            .withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.EXPIRATION_TIME))
            // 비공개 클래임으로 키-밸류 값 
            .withClaim("id", principalDetails.getUser().getId())
            .withClaim("userId", principalDetails.getUser().getUserId())
            .sign(Algorithm.HMAC512(JwtProperties.SECRET));

        response.addHeader(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX + jwtToken);

        // 성공 메시지 작성
        response.setStatus(HttpStatus.ACCEPTED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        // JSON으로 만들기 위해 Map으로 데이터 저장 
        Map<String, Object> errorMessage = new HashMap<>();               
        errorMessage.put("msg", "Login Success");

        // JSON 형태로 메시지 생성
        new ObjectMapper().writeValue(response.getOutputStream(), errorMessage);

    }

}

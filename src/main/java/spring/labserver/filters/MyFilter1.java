package spring.labserver.filters;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// 인증
public class MyFilter1 implements Filter{

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        // 한글 깨지지 않게 utf-8 설정
        res.setCharacterEncoding("UTF-8");
        res.setContentType("text/html; UTF-8");

        // JWT 기본 작동 방식
        // ID, PW가 정상적으로 들어와서 로그인이 완료 되면 토큰을 만들어주고 그걸 client에 응답 해줘야 함
        // client가 요청할 때마다 header의 Authorization에 value 값으로 토큰을 가져옴
        // 그 때 토큰이 넘어오면 해당 토큰이 서버가 만든 토큰인지만 검증하면 됨(RSA, HS256)

        // POST방식인 경우 request의 Authorization을 받아옴
        if(req.getMethod().equals("POST")) {
            System.out.println("POST 요청됨");
            String headerAuth = req.getHeader("Authorization");
            System.out.println(headerAuth);
            System.out.println("필터 1");
            if(headerAuth.equals("cos")) {
                chain.doFilter(req, res);
            } else {
                PrintWriter out = res.getWriter();
                out.println("인증안됨");
            }
        }

    }
    
}

package spring.labserver.config.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class JwtTokenProvider {
    private String encodeJwt;

    @Builder
    public JwtTokenProvider(String encodeJwt) {
        this.encodeJwt = encodeJwt;
    }

    public String getUserIdFromJWT() {
        String jwtToken = encodeJwt.replace(JwtProperties.TOKEN_PREFIX, "");
        String userId = JWT.require(Algorithm.HMAC512(JwtProperties.SECRET)).build().verify(jwtToken).getClaim("userId").asString();
        return userId;
    }

}

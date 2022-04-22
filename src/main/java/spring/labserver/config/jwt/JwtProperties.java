package spring.labserver.config.jwt;

public interface JwtProperties {
    String SECRET = "secretKey";
    int EXPIRATION_TIME = 60000 * 30;
    String TOKEN_PREFIX = "Bearer ";
    String HEADER_STRING = "Authorization";
}
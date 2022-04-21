package spring.labserver.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

// 모든 필드의 get 메소드 생성
@Getter
// final 필드의 생성자 생성
@RequiredArgsConstructor
public class UserResponseDto {
    
    private final String userId;
    private final String name;
    private final String password;
    private final String phone;
    private final String mail;
    private final String role;
}

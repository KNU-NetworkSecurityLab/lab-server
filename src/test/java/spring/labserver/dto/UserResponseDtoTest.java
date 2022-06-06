package spring.labserver.dto;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class UserResponseDtoTest {

    @Test
    public void userDtoTest() {
        String userId = "testID";
        String password = "testPw";
        String name = "홍길동";
        String phone = "010-1111-1111";
        String mail = "testMail@naver.com";
        String role = "USER";

        UserResponseDto dto = new UserResponseDto(userId, name, password, phone, mail, role);

        assertThat(dto.getUserId()).isEqualTo(userId);
        assertThat(dto.getName()).isEqualTo(name);
        assertThat(dto.getPhone()).isEqualTo(phone);
        assertThat(dto.getMail()).isEqualTo(mail);
        assertThat(dto.getRole()).isEqualTo(role);
    }
    
}

package spring.labserver.dto;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class UserResponseDtoTest {

    @Test
    public void userDtoTest() {
        String id = "testID";
        String name = "홍길동";
        String phone = "010-1111-1111";
        String mail = "testMail@naver.com";

        UserResponseDto dto = new UserResponseDto(id, name, phone, mail);

        assertThat(dto.getId()).isEqualTo(id);
        assertThat(dto.getName()).isEqualTo(name);
        assertThat(dto.getPhone()).isEqualTo(phone);
        assertThat(dto.getMail()).isEqualTo(mail);
    }
    
}

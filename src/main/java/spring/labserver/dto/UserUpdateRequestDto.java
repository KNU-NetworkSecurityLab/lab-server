package spring.labserver.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserUpdateRequestDto {
    private String password;
    private String phone;
    private String mail;
    private String name;

    @Builder
    public UserUpdateRequestDto( String password, String phone, String mail, String name) {
        this.password = password;
        this.phone = phone;
        this.mail = mail;
        this.name = name;
    }
}

package spring.labserver.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserResetPasswordRequestDto {
    private String userId;
    private String name;
    private String mail;

    @Builder
    public UserResetPasswordRequestDto(String userId, String name, String mail) {
        this.userId = userId;
        this.name = name;
        this.mail = mail;
    }
}

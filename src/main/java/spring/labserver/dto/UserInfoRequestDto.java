package spring.labserver.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserInfoRequestDto {
    private String userId;

    @Builder
    public UserInfoRequestDto(String userId) {
        this.userId = userId;
    }
}

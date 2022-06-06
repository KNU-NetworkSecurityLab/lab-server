package spring.labserver.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserRoleUpdateRequestDto {
    private String userId;
    private String role;

    @Builder
    public UserRoleUpdateRequestDto(String userId, String role) {
        this.userId = userId;
        this.role = role;
    }
    
}

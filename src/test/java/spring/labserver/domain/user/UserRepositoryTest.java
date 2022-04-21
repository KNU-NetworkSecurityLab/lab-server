package spring.labserver.domain.user;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class UserRepositoryTest {
    
    @Autowired
    UserRepository userRepository;

    // 단위 테스트가 끝날 때마다 수행할 메소드
    @AfterEach
    public void cleanup() {
        userRepository.deleteAll();
    }

    @Test
    public void 유저정보_불러오기() {
        String userId = "testId";
        String name = "testName";
        String password = "testPw";
        String mail = "testMail@naver.com";
        String phone = "010-1111-1111";
        String role = "ROLE_USER";

        userRepository.save(User.builder()
            .userId(userId)
            .name(name)
            .password(password)
            .mail(mail)
            .phone(phone)
            .role(role)
            .build());
        
        List<User> userList = userRepository.findAll();
        
        User user = userList.get(0);
        assertThat(user.getUserId()).isEqualTo(userId);
        assertThat(user.getName()).isEqualTo(name);
        assertThat(user.getPassword()).isEqualTo(password);
        assertThat(user.getMail()).isEqualTo(mail);
        assertThat(user.getPhone()).isEqualTo(phone);
        assertThat(user.getRole()).isEqualTo(role);
    }
}

package spring.labserver.domain.user;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@TestPropertySource(properties = { "spring.config.location=classpath:application-test.yml" })
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTest {
    
    @Autowired
    private UserRepository userRepository;

    // DataJpaTest는 테스트 종료 후 롤백도 같이 실행 됨
    @Test
    @WithMockUser(roles = "USER")
    public void 유저정보_불러오기() {
        
        // given
        String userId = "testId";
        String name = "testName";
        String password = "testPw";
        String mail = "testMail@naver.com";
        String phone = "010-1111-1111";
        String role = "ROLE_USER";

        // when
        userRepository.save(User.builder()
            .userId(userId)
            .name(name)
            .password(password)
            .mail(mail)
            .phone(phone)
            .role(role)
            .build());

        List<User> userList = userRepository.findAll();
        
        // then
        User user = userList.get(0);
        assertThat(user.getUserId()).isEqualTo(userId);
        assertThat(user.getName()).isEqualTo(name);
        assertThat(user.getPassword()).isEqualTo(password);
        assertThat(user.getMail()).isEqualTo(mail);
        assertThat(user.getPhone()).isEqualTo(phone);
        assertThat(user.getRole()).isEqualTo(role);
    }
}

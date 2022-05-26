package spring.labserver.domain.user;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import spring.labserver.domain.user.query.UserInfoInterface;
import spring.labserver.dto.UserUpdateRequestDto;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@TestPropertySource(properties = { "spring.config.location=classpath:application-test.yml" })
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTest {
    
    @Autowired
    private UserRepository userRepository;

    // given
    private String userId1 = "testId1";
    private String name1 = "testName1";
    private String password1 = "testPw1";
    private String mail1 = "testMail@naver.com";
    private String phone1 = "010-1111-1111";
    private String role1 = "ROLE_USER";

    private String userId2 = "testId2";
    private String name2 = "testName2";
    private String password2 = "testPw2";
    private String mail2 = "testMail@naver.com";
    private String phone2 = "010-1111-1111";
    private String role2 = "ROLE_USER";

    @BeforeEach
    void before() {
        userRepository.save(User.builder()
            .userId(userId1)
            .name(name1)
            .password(password1)
            .mail(mail1)
            .phone(phone1)
            .role(role1)
            .build());

        userRepository.save(User.builder()
            .userId(userId2)
            .name(name2)
            .password(password2)
            .mail(mail2)
            .phone(phone2)
            .role(role2)
            .build());
    }

    // DataJpaTest는 테스트 종료 후 롤백도 같이 실행 됨
    @Test
    @WithMockUser(roles = "USER")
    public void 유저정보_불러오기() {
        // when
        List<User> userList = userRepository.findAll();
        
        // then
        User user = userList.get(0);
        assertThat(user.getUserId()).isEqualTo(userId1);
        assertThat(user.getName()).isEqualTo(name1);
        assertThat(user.getPassword()).isEqualTo(password1);
        assertThat(user.getMail()).isEqualTo(mail2);
        assertThat(user.getPhone()).isEqualTo(phone1);
        assertThat(user.getRole()).isEqualTo(role1);
    }

    @Test
    @WithMockUser(roles = "USER")
    public void 유저리스트_불러오기() {
        // when
        List<UserInfoInterface> userInfoList = userRepository.findAllUserInfoByRole();
        
        int i = 0;
        // then
        for(UserInfoInterface tmp : userInfoList) {
            if(i == 0) {
                assertThat(tmp.getUserId()).isEqualTo(userId1);
                assertThat(tmp.getName()).isEqualTo(name1);
                assertThat(tmp.getMail()).isEqualTo(mail1);
                assertThat(tmp.getPhone()).isEqualTo(phone1);    
            } else {
                assertThat(tmp.getUserId()).isEqualTo(userId2);
                assertThat(tmp.getName()).isEqualTo(name2);
                assertThat(tmp.getMail()).isEqualTo(mail2);
                assertThat(tmp.getPhone()).isEqualTo(phone2);    
            }
            i++;
        }
    }

    @Test
    @WithMockUser(roles = "USER")
    public void 유저정보_수정하기() {
        // when
        List<User> userList = userRepository.findAll();
                
        // then
        User user = userList.get(0);
        String ChangedPassword = "ChangedPassword";
        String ChangedMail = "ChangedMail";
        String ChangedPhone = "ChangedPhone";

        // DTO 테스트
        UserUpdateRequestDto requestDto = UserUpdateRequestDto.builder()
            .userId(user.getUserId())
            .password(ChangedPassword)
            .mail(ChangedMail)
            .phone(ChangedPhone)
            .build();
                
        // 갱신 테스트
        user.update(requestDto.getPassword(), requestDto.getPhone(), requestDto.getMail());
        assertThat(user.getUserId()).isEqualTo(userId1);
        assertThat(user.getPassword()).isEqualTo(ChangedPassword);
        assertThat(user.getMail()).isEqualTo(ChangedMail);
        assertThat(user.getPhone()).isEqualTo(ChangedPhone);
    }
}

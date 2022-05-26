package spring.labserver.services;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import spring.labserver.domain.user.User;
import spring.labserver.domain.user.UserRepository;
import spring.labserver.domain.user.query.UserInfoInterface;
import spring.labserver.dto.UserMsgResponseDto;
import spring.labserver.dto.UserUpdateRequestDto;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    // userId로 회원 정보 찾기
    @Transactional
    public User findByUserId(String userId) {
        return userRepository.findByUserId(userId);
    }

    // 회원 가입
    @Transactional
    public UserMsgResponseDto save(User user) {
        UserMsgResponseDto responseDto = new UserMsgResponseDto();

        // bCryptPasswordEncoder는 비밀번호를 암호화 하는데 사용 됨
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setRole("ROLE_USER");        

         // 중복된 아이디가 있는지 확인
        if(userRepository.existsByUserId(user.getUserId())) {
            responseDto.setMsg("Duplicated userId");
            return responseDto;
        } else {
            userRepository.save(user);
            responseDto.setMsg("SignUp Success");
            return responseDto;
        }
    }

    // 회원 정보 갱신
    @Transactional
    public UserMsgResponseDto update(UserUpdateRequestDto requestDto) {
        UserMsgResponseDto responseDto = new UserMsgResponseDto();

        // 해당 아이디가 있는지 확인 후 update
        if(userRepository.existsByUserId(requestDto.getUserId())) {
            User user = userRepository.findByUserId(requestDto.getUserId());
            user.update(bCryptPasswordEncoder.encode(requestDto.getPassword()), requestDto.getPhone(), requestDto.getMail());
            responseDto.setMsg("Update Success");
            return responseDto;
        // 해당 아이디가 없다면
        } else {
            responseDto.setMsg("Not Existing ID");
            return responseDto;
        }
    }

    @Transactional
    public List<UserInfoInterface> findAllUserInfoByRole() {
        List<UserInfoInterface> userList = userRepository.findAllUserInfoByRole();        
        return userList;
    }    


}

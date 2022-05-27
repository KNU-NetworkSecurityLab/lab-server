package spring.labserver.services;

import javax.transaction.Transactional;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import spring.labserver.domain.user.User;
import spring.labserver.domain.user.UserRepository;
import spring.labserver.dto.UserUpdateRequestDto;
import spring.labserver.error.exception.UserAlreadyExistException;
import spring.labserver.error.exception.UserNotExistException;
import spring.labserver.error.exception.UserNullException;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    // 해당 사용자의 모든 정보 조회 - JWT PrincipalDetailsService에 사용
    @Transactional
    public User findByUserId(String userId) {
        if(userId == null | userId.length() == 0) {
            throw new UserNullException();
        }

        return userRepository.findByUserId(userId);
    }

    // 회원가입
    @Transactional
    public ResponseEntity<Object> save(User user) {
        // user 중에 NULL이 있다면        
        if(user.getUserId() == null | user.getMail() == null | user.getPassword() == null | user.getPhone() == null | user.getName() == null
         | user.getUserId().length() == 0 | user.getMail().length() == 0 | user.getPassword().length() == 0 | user.getPhone().length() == 0 | user.getName().length() == 0) {            
            throw new UserNullException();
        }

        // bCryptPasswordEncoder는 비밀번호를 암호화 하는데 사용 됨
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setRole("ROLE_USER");        

         // 아이디가 중복 되었다면
        if(userRepository.existsByUserId(user.getUserId())) {
            throw new UserAlreadyExistException();
        // 회원가입 성공
        } else {
            userRepository.save(user);
            return ResponseEntity.ok("SignUp Success");
        }
    }

    // 회원 정보 갱신
    @Transactional
    public ResponseEntity<String> update(UserUpdateRequestDto requestDto) {
        // requestDto 중에 NULL이 있다면        
        if(requestDto.getMail() == null | requestDto.getPassword() == null | requestDto.getPhone() == null | requestDto.getName() == null
        | requestDto.getMail().length() == 0 | requestDto.getPassword().length() == 0 | requestDto.getPhone().length() == 0 | requestDto.getName().length() == 0) {            
            throw new UserNullException();
        }

        // 해당 아이디가 있는지 확인 후 update
        if(userRepository.existsByUserId(requestDto.getUserId())) {
            User user = userRepository.findByUserId(requestDto.getUserId());
            user.update(bCryptPasswordEncoder.encode(requestDto.getPassword()), requestDto.getPhone(), requestDto.getMail());
            return ResponseEntity.ok("Update Success");
        // 해당 아이디가 없다면
        } else {
            throw new UserNotExistException();
        }
    }

    // 자신의 회원 정보 조회
    @Transactional
    public ResponseEntity<Object> findUserInfoById(String userId) {
        if(userId == null | userId.length() == 0) {
            throw new UserNullException();
        }
        
         // 해당 아이디가 있는지 확인
         if(userRepository.existsByUserId(userId)) {
            return ResponseEntity.ok().body(userRepository.findUserInfoById(userId));
        // 해당 아이디가 없다면
        } else {            
            throw new UserNotExistException();
        }
             
    }
    
    // 여러 사용자들의 회원 정보 조회
    @Transactional
    public ResponseEntity<Object> findAllUserInfoByRole() {       
        return ResponseEntity.ok().body(userRepository.findAllUserInfoByRole());
    }    


}

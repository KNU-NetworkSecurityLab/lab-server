package spring.labserver.services;

import javax.transaction.Transactional;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import spring.labserver.config.jwt.JwtTokenProvider;
import spring.labserver.domain.user.User;
import spring.labserver.domain.user.UserRepository;
import spring.labserver.dto.UserRoleUpdateRequestDto;
import spring.labserver.dto.UserUpdateRequestDto;
import spring.labserver.error.exception.UserAlreadyExistException;
import spring.labserver.error.exception.UserNotAdminException;
import spring.labserver.error.exception.UserNotExistException;
import spring.labserver.error.exception.UserNullException;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;    
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private JwtTokenProvider jwtTokenProvider;

    // 해당 사용자의 모든 정보 조회 - JWT PrincipalDetailsService에 사용
    @Transactional
    public User findByUserId(String userId) {
        if(userId == null | userId.length() == 0) {
            throw new UserNullException();
        }

        return userRepository.findByUserId(userId);
    }

    // USER
    // 여러 사용자들의 회원 정보 조회
    @Transactional
    public ResponseEntity<Object> findAllUserInfoByRole() {   
        if(userRepository.findAllUserInfoByRole() == null) {
            throw new UserNullException();
        }     
        return ResponseEntity.ok().body(userRepository.findAllUserInfoByRole());
    }   

    // 회원가입
    @Transactional
    public ResponseEntity<Object> save(User user) {
        // user 중에 NULL이 있다면        
        if(user.getUserId() == null | user.getMail() == null | user.getPassword() == null | user.getPhone() == null | user.getName() == null | user.getPosition() == null | user.getStudentId() == null
         | user.getUserId().length() == 0 | user.getMail().length() == 0 | user.getPassword().length() == 0 | user.getPhone().length() == 0 | user.getName().length() == 0 | user.getPosition().length() == 0 | user.getStudentId().length() == 0) {            
            throw new UserNullException();
        }

        // bCryptPasswordEncoder는 비밀번호를 암호화 하는데 사용 됨
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setRole("ROLE_GUEST");        

         // 아이디가 중복 되었다면
        if(userRepository.existsByUserId(user.getUserId())) {
            throw new UserAlreadyExistException();
        // 회원가입 성공
        } else {
            userRepository.save(user);
            return ResponseEntity.ok("SignUp Success");
        }
    }

    // 자신의 회원 정보 조회
    @Transactional
    public ResponseEntity<Object> findUserInfoById(String token) {
        // 요청한 userId와 현재 로그인한 userId가 다르다면
        jwtTokenProvider = JwtTokenProvider.builder().encodeJwt(token).build();
        String userId = jwtTokenProvider.getUserIdFromJWT();

        // userId가 NULL이면
        if(userId == null | userId.length() == 0) {
            throw new UserNullException();
        }
        
        // 해당 아이디가 있다면
         if(userRepository.existsByUserId(userId)) {
            return ResponseEntity.ok().body(userRepository.findUserInfoById(userId));
        // 해당 아이디가 없다면
        } else {            
            throw new UserNotExistException();
        }
             
    }

    // 회원 정보 갱신
    @Transactional
    public ResponseEntity<String> update(String token, UserUpdateRequestDto requestDto) {
        // 요청한 userId와 현재 로그인한 userId가 다르다면
        jwtTokenProvider = JwtTokenProvider.builder().encodeJwt(token).build();
        String userId = jwtTokenProvider.getUserIdFromJWT();

        // requestDto 중에 NULL이면        
        if(userId == null | requestDto.getMail() == null | requestDto.getPassword() == null | requestDto.getPhone() == null | requestDto.getName() == null | requestDto.getPosition() == null | requestDto.getStudentId() == null
        | userId.length() == 0 | requestDto.getMail().length() == 0 | requestDto.getPassword().length() == 0 | requestDto.getPhone().length() == 0 | requestDto.getName().length() == 0 | requestDto.getPosition().length() == 0 | requestDto.getStudentId().length() == 0) {            
            throw new UserNullException();
        }

        // 해당 아이디가 있는지 확인 후 update
        if(userRepository.existsByUserId(userId)) {
            User user = userRepository.findByUserId(userId);
            user.update(bCryptPasswordEncoder.encode(requestDto.getPassword()), requestDto.getPhone(), requestDto.getMail(), requestDto.getPosition(), requestDto.getStudentId());
            return ResponseEntity.ok("Update Success");
        // 해당 아이디가 없다면
        } else {
            throw new UserNotExistException();
        }
    }

    // 회원 탈퇴
    @Transactional
    public ResponseEntity<String> delete(String token) {
        // 요청한 userId와 현재 로그인한 userId가 다르다면
        jwtTokenProvider = JwtTokenProvider.builder().encodeJwt(token).build();
        String userId = jwtTokenProvider.getUserIdFromJWT();

        // userId가 NULL이면
        if(userId == null | userId.length() == 0) {
            throw new UserNullException();
        }
        
        // 해당 아이디가 있는지 확인 후 delete
        if(userRepository.existsByUserId(userId)) {
            userRepository.deleteByUserId(userId);
            return ResponseEntity.ok("Delete Success");
        // 해당 아이디가 없다면
        } else {
            throw new UserNotExistException();
        }
    }     

    // ADMIN
    // ADMIN이 회원들의 직위 변경
    @Transactional
    public ResponseEntity<String> setRole(String token, UserRoleUpdateRequestDto requestDto) {   
        // 요청한 userId와 현재 로그인한 userId가 Admin 인지 확인
        jwtTokenProvider = JwtTokenProvider.builder().encodeJwt(token).build();
        String userId = jwtTokenProvider.getUserIdFromJWT();
        // userId가 NULL이거나 받은 데이터 중 NULL이 있을 때
        if(userId == null | requestDto.getUserId() == null | requestDto.getRole() == null 
        | userId.length() == 0 | requestDto.getUserId().length() == 0 | requestDto.getRole().length() == 0) {
            throw new UserNullException();
        }

        // ADMIN 아이디가 있는지 확인
        if(userRepository.existsByUserId(userId)) {
            User admin = userRepository.findByUserId(userId);

            // ADMIN이 아니면
            if(!userId.equals("admin") | admin.getRole() != "ROLE_ADMIN") {
                throw new UserNotAdminException();
            }

        // 해당 아이디가 없다면
        } else {
            throw new UserNotExistException();
        }            

        // 권한을 수정하려는 아이디가 있는지 확인 후 update
        if(userRepository.existsByUserId(requestDto.getUserId())) {
            User user = userRepository.findByUserId(requestDto.getUserId());
            user.setRole(requestDto.getRole());
            return ResponseEntity.ok("Role Update Success");
        // 해당 아이디가 없다면
        } else {
            throw new UserNotExistException();
        }
    } 
}

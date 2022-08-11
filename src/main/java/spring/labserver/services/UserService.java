package spring.labserver.services;

import java.util.HashMap;
import java.util.Map;

import javax.transaction.Transactional;

import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import spring.labserver.config.jwt.JwtTokenProvider;
import spring.labserver.domain.user.User;
import spring.labserver.domain.user.UserRepository;
import spring.labserver.dto.UserResetPasswordRequestDto;
import spring.labserver.dto.UserRoleUpdateRequestDto;
import spring.labserver.dto.UserUpdateRequestDto;
import spring.labserver.error.exception.RequestDeniedException;
import spring.labserver.error.exception.UserAlreadyExistException;
import spring.labserver.error.exception.UserNotAdminException;
import spring.labserver.error.exception.UserNotExistException;
import spring.labserver.error.exception.UserNullException;

@RequiredArgsConstructor
@Service
public class UserService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private final UserRepository userRepository;    
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final MailService mailService;
    private JwtTokenProvider jwtTokenProvider;

    // 해당 사용자의 모든 정보 조회 - JWT PrincipalDetailsService에 사용
    @Transactional
    public User findByUserId(String userId) {
        if(userId == null) {
            throw new UserNullException();
        }
        if(userId.length() == 0) {
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
        if(user.getUserId() == null | user.getMail() == null | user.getPassword() == null | user.getPhone() == null | user.getName() == null | user.getPosition() == null | user.getStudentId() == null) {            
            throw new UserNullException();
        }
        if(user.getUserId().length() == 0 | user.getMail().length() == 0 | user.getPassword().length() == 0 | user.getPhone().length() == 0 | user.getName().length() == 0 | user.getPosition().length() == 0 | user.getStudentId().length() == 0) {
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

            Map<String, String> result = new HashMap<>();
            result.put("msg", "SignUp Success");
            return ResponseEntity.ok().body(result);
        }
    }

    // 자신의 회원 정보 조회
    @Transactional
    public ResponseEntity<Object> findUserInfoById(String token) {
        // 요청한 userId와 현재 로그인한 userId가 다르다면
        jwtTokenProvider = JwtTokenProvider.builder().encodeJwt(token).build();
        String userId = jwtTokenProvider.getUserIdFromJWT();

        // userId가 NULL이면
        if(userId == null) {
            throw new UserNullException();
        }
        if(userId.length() == 0) {
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
    public ResponseEntity<Object> update(String token, UserUpdateRequestDto requestDto) {
        // requestDto 중에 NULL이면        
        if(requestDto.getUserId() == null | requestDto.getMail() == null | requestDto.getPassword() == null | requestDto.getPhone() == null | requestDto.getName() == null | requestDto.getPosition() == null | requestDto.getStudentId() == null) {            
            throw new UserNullException();
        }
        if(requestDto.getUserId().length() == 0 | requestDto.getMail().length() == 0 | requestDto.getPassword().length() == 0 | requestDto.getPhone().length() == 0 | requestDto.getName().length() == 0 | requestDto.getPosition().length() == 0 | requestDto.getStudentId().length() == 0) {
            throw new UserNullException();
        }

        jwtTokenProvider = JwtTokenProvider.builder().encodeJwt(token).build();
        String userId = jwtTokenProvider.getUserIdFromJWT();
        // 요청한 userId와 현재 로그인한 userId가 다르다면
        if (!userId.equals(requestDto.getUserId())) {
            throw new RequestDeniedException();
        }

        // 해당 아이디가 DB에 있는지 확인 후 update
        if(userRepository.existsByUserId(userId)) {
            User user = userRepository.findByUserId(userId);
            user.update(requestDto.getName(), bCryptPasswordEncoder.encode(requestDto.getPassword()), requestDto.getPhone(), requestDto.getMail(), requestDto.getPosition(), requestDto.getStudentId());

            Map<String, String> result = new HashMap<>();
            result.put("msg", "Update Success");
            return ResponseEntity.ok().body(result);
        // 해당 아이디가 없다면
        } else {
            throw new UserNotExistException();
        }
    }

    // 회원 탈퇴
    @Transactional
    public ResponseEntity<Object> delete(String token) {
        // JWT 토큰으로부터 현재 로그인한 userId 가져오기
        jwtTokenProvider = JwtTokenProvider.builder().encodeJwt(token).build();
        String userId = jwtTokenProvider.getUserIdFromJWT();

        // userId가 NULL이면
        if(userId == null) {
            throw new UserNullException();
        }
        if(userId.length() == 0) {
            throw new UserNullException();
        }
        
        // 해당 아이디가 있는지 확인 후 delete
        if(userRepository.existsByUserId(userId)) {
            userRepository.deleteByUserId(userId);

            Map<String, String> result = new HashMap<>();
            result.put("msg", "Delete Success");
            return ResponseEntity.ok().body(result);
        // 해당 아이디가 없다면
        } else {
            throw new UserNotExistException();
        }
    }   
    
    // 비밀번호 초기화
    @Transactional
    public ResponseEntity<Object> resetPassword(UserResetPasswordRequestDto requestDto) {

        // requestDto 중에 NULL이면        
        if(requestDto.getUserId() == null | requestDto.getMail() == null | requestDto.getName() == null) {            
            throw new UserNullException();
        }
        if(requestDto.getUserId().length() == 0 | requestDto.getMail().length() == 0 | requestDto.getName().length() == 0) {
            throw new UserNullException();
        }

        // 해당 아이디가 있는지 확인 후 비밀번호 초기화 진행
        if(userRepository.existsByUserId(requestDto.getUserId())) {
            User user = userRepository.findByUserId(requestDto.getUserId());
            if(user.getMail().equals(requestDto.getMail()) && user.getName().equals(requestDto.getName())) {
                // 아스키 코드 33 ~ 125 사이에서 문자를 골라서 15자리 임시비밀번호 생성
                String tempPassword = RandomStringUtils.random(15, 33, 125, false, false);
                // 임시 비밀번호 생성, DB 적용
                user.update(user.getName(), bCryptPasswordEncoder.encode(tempPassword), user.getPhone(), user.getMail(), user.getPosition(), user.getStudentId());
                // 메일 전송
                mailService.sendMail(user.getMail(), tempPassword);
                //
                Map<String, String> result = new HashMap<>();
                result.put("msg", "Reset Password Success and Mail Send");
                return ResponseEntity.ok().body(result);
            // 해당 이메일 또는 이름을 가진 아이디가 없다면
            } else {
                throw new UserNotExistException();
            }        
        // 해당 아이디가 없다면
        } else {
            throw new UserNotExistException();
        }
    } 

    // ADMIN
    // ADMIN이 회원들의 직위 변경
    @Transactional
    public ResponseEntity<Object> setRole(String token, UserRoleUpdateRequestDto requestDto) {   
        // 현재 로그인한 userId가 Admin 인지 확인
        jwtTokenProvider = JwtTokenProvider.builder().encodeJwt(token).build();
        String userId = jwtTokenProvider.getUserIdFromJWT();
        // userId가 NULL이거나 받은 데이터 중 NULL이 있을 때
        if(userId == null | requestDto.getUserId() == null | requestDto.getRole() == null) {
            throw new UserNullException();
        }
        if(userId.length() == 0 | requestDto.getUserId().length() == 0 | requestDto.getRole().length() == 0) {
            throw new UserNullException();
        }

        // ADMIN 아이디가 있는지 확인
        if(userRepository.existsByUserId(userId)) {
            User admin = userRepository.findByUserId(userId);

            // ADMIN이 아니면
            if(!userId.equals("admin") | !admin.getRole().equals("ROLE_ADMIN")) {
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

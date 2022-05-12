package spring.labserver.controllers;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import spring.labserver.domain.user.User;
import spring.labserver.domain.user.UserRepository;
import spring.labserver.dto.UserResponseDto;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class UserController {
    
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    // 회원 가입
    @PostMapping("/signup")
    public String signup(@RequestBody User user) {
        // bCryptPasswordEncoder는 비밀번호를 암호화 하는데 사용 됨
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setRole("ROLE_USER");

        // 중복된 아이디가 있는지 확인
        if(userRepository.existsByUserId(user.getUserId())) {
            return "fail: 중복된 아이디가 있습니다.";
        } else {
            userRepository.save(user);
            return "success";    
        }
    }

    // USER, ADMIN 접근 가능
    @GetMapping("/user")
    public String user() {
        return "user";
    }

    // ADMIN 접근 가능
    @GetMapping("/admin")
    public String admin() {
        return "admin";
    }

    @GetMapping("/user/dto")
    public UserResponseDto userDto(@RequestParam("userId") String userId, @RequestParam("name") String name, @RequestParam("password") String password,
        @RequestParam("phone") String phone, @RequestParam("mail") String mail, @RequestParam String role) {
            return new UserResponseDto(userId, name, password, phone, mail, role);
    }
    
    

}

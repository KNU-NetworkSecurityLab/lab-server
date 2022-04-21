package spring.labserver.controllers;

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
@RequestMapping("api/v1")
@RequiredArgsConstructor
public class UserController {
    
    private final UserRepository userRepository;

    // USER, ADMIN 접근 가능
    @GetMapping("user")
    public String user() {
        return "user";
    }

    // ADMIN 접근 가능
    @GetMapping("admin")
    public String admin() {
        return "admin";
    }

    @GetMapping("/user/dto")
    public UserResponseDto userDto(@RequestParam("userId") String userId, @RequestParam("name") String name, @RequestParam("password") String password,
        @RequestParam("phone") String phone, @RequestParam("mail") String mail, @RequestParam String role) {
            return new UserResponseDto(userId, name, password, phone, mail, role);
    }
    
    @PostMapping("/join")
    public String join(@RequestBody User user) {
        user.setPassword(user.getPassword());
        user.setRole("ROLE_USER");
        userRepository.save(user);
        return "회원가입완료";
    }
}

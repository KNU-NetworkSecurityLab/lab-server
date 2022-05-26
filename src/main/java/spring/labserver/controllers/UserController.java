package spring.labserver.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import spring.labserver.domain.user.User;
import spring.labserver.domain.user.query.UserInfoInterface;
import spring.labserver.dto.UserMsgResponseDto;
import spring.labserver.dto.UserResponseDto;
import spring.labserver.dto.UserUpdateRequestDto;
import spring.labserver.services.UserService;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class UserController {
    
    private final UserService userService;

    // USER, ADMIN 접근 가능
    @GetMapping("/user")
    public List<UserInfoInterface> user() {
        return userService.findAllUserInfoByRole();        
    }

    // 회원 가입
    @PostMapping("/signup")
    public UserMsgResponseDto signup(@RequestBody User user) {
        return userService.save(user);
    }

    // 회원 정보 갱신
    @GetMapping("/user/update")
    public UserMsgResponseDto userUpdate(@RequestBody UserUpdateRequestDto requestDto) {        
        return userService.update(requestDto);
    }


    @GetMapping("/user/dto")
    public UserResponseDto userDto(@RequestParam("userId") String userId, @RequestParam("name") String name, @RequestParam("password") String password,
        @RequestParam("phone") String phone, @RequestParam("mail") String mail, @RequestParam String role) {
        return new UserResponseDto(userId, name, password, phone, mail, role);
    }
    
    // ADMIN 접근 가능
    @GetMapping("/admin")
    public String admin() {
        return "admin";
    }

}

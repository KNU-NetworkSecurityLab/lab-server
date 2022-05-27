package spring.labserver.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import spring.labserver.domain.user.User;
import spring.labserver.dto.UserInfoRequestDto;
import spring.labserver.dto.UserUpdateRequestDto;
import spring.labserver.services.UserService;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class UserController {
    
    private final UserService userService;

    // ResponseEntity 사용해서 에러처리하기
    // 회원 가입
    @PostMapping("/signup")
    public ResponseEntity<Object> signup(@RequestBody User user) {
        return userService.save(user);
    }

    // USER, ADMIN 접근 가능
    // ROLE_USER 권한을 가진 사용자들의 정보 반환(Password 제외)
    @GetMapping("/user")
    public ResponseEntity<Object> user() {
        return userService.findAllUserInfoByRole();        
    }

    // 자신의 회원 정보 조회
    @PostMapping("/user/info")
    public ResponseEntity<Object> userInfo(@RequestBody UserInfoRequestDto userInfoRequestDto) {
        return userService.findUserInfoById(userInfoRequestDto.getUserId());
    }

    // 회원 정보 갱신
    @GetMapping("/user/update")
    public ResponseEntity<String> userUpdate(@RequestBody UserUpdateRequestDto requestDto) {        
        return userService.update(requestDto);
    }

    // @GetMapping("/user/dto")
    // public UserResponseDto userDto(@RequestParam("userId") String userId, @RequestParam("name") String name, @RequestParam("password") String password,
    //     @RequestParam("phone") String phone, @RequestParam("mail") String mail, @RequestParam String role) {
    //     return new UserResponseDto(userId, name, password, phone, mail, role);
    // }
    
    // ADMIN 접근 가능
    @GetMapping("/admin")
    public String admin() {
        return "admin";
    }

}

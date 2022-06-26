package spring.labserver.controllers;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import spring.labserver.domain.user.User;
import spring.labserver.dto.UserRoleUpdateRequestDto;
import spring.labserver.dto.UserUpdateRequestDto;
import spring.labserver.services.UserService;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class UserController {
    
    private final UserService userService;
    
    // USER, ADMIN 접근 가능
    // ROLE_USER 권한을 가진 사용자들의 정보 반환(Password 제외)
    @GetMapping("/user")
    public ResponseEntity<Object> user() {
        return userService.findAllUserInfoByRole();        
    }

    // ResponseEntity 사용해서 에러처리하기
    // 회원 가입
    @PostMapping("/signup")
    public ResponseEntity<Object> signup(@RequestBody User user) {
        return userService.save(user);
    }

    // 자신의 회원 정보 조회
    @PostMapping("/user/info")
    public ResponseEntity<Object> userInfo(@RequestHeader("Authorization") String token) {
        return userService.findUserInfoById(token);
    }

    // 회원 정보 갱신
    @PutMapping("/user/update")
    public ResponseEntity<Object> userUpdate(@RequestHeader("Authorization") String token, @RequestBody UserUpdateRequestDto requestDto) {        
        return userService.update(token, requestDto);
    }

    // 회원 탈퇴
    @DeleteMapping("/user/delete")
    public ResponseEntity<Object> userDelete(@RequestHeader("Authorization") String token) {        
        return userService.delete(token);
    }    
    
    // ADMIN만 접근 가능
    // 사용자 권한 수정
    @PutMapping("/admin/authority")
    public ResponseEntity<Object> adminRole(@RequestHeader("Authorization") String token, @RequestBody UserRoleUpdateRequestDto requestDto) {
        return userService.setRole(token, requestDto);
    }

}

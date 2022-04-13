package spring.labserver.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import spring.labserver.dto.UserResponseDto;

@RestController
public class UserController {

    @GetMapping("/user")
    public String user() {
        return "user";
    }

    @GetMapping("/user/dto")
    public UserResponseDto userDto(@RequestParam("id") String id, @RequestParam("name") String name,
        @RequestParam("phone") String phone, @RequestParam("mail") String mail) {
            return new UserResponseDto(id, name, phone, mail);
        }
    
}

package spring.labserver.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MainController {
    
    @GetMapping("/")
    @ResponseBody
    public String home() {
        return "Hello World!";
    }

    @PostMapping("/token")
    @ResponseBody
    public String token() {
        return "Hello token!";
    }
}

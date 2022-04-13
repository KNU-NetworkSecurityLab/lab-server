package spring.labserver.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = UserController.class)
public class UserControllerTest {
    
    @Autowired
    private MockMvc mvc;

    @Test
    public void user_테스트() throws Exception {
        String user = "user";

        mvc.perform(get("/user"))
            .andExpect(status().isOk())
            .andExpect(content().string(user));
    }

    @Test
    public void userDto_테스트() throws Exception {
        String id = "testID";
        String name = "홍길동";
        String phone = "010-1111-1111";
        String mail = "testMail@naver.com";

        mvc.perform(get("/user/dto")
        .param("id", id).param("name", name).param("phone", phone).param("mail", mail))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id", is(id)))
        .andExpect(jsonPath("$.name", is(name)))
        .andExpect(jsonPath("$.phone", is(phone)))
        .andExpect(jsonPath("$.mail", is(mail)));
        
    }
}

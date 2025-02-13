package com.shyampatel.webapp.githubplayroom.user;

import com.shyampatel.webapp.githubplayroom.TestConfig;
import com.shyampatel.webapp.githubplayroom.config.GithubPlayroomSecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@Import({GithubPlayroomSecurityConfig.class, TestConfig.class})
@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testCall() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users/test")).andDo(print()).
                andExpect(status().isOk()).andExpect(content().string(equalTo("admin")));
    }
}
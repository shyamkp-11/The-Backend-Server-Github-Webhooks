package com.shyampatel.webapp.githubplayroom.auth;

import com.shyampatel.webapp.githubplayroom.user.Role;
import com.shyampatel.webapp.githubplayroom.user.User;
import com.shyampatel.webapp.githubplayroom.user.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest()
//@WebMvcTest(controllers = AuthenticationController.class)
@ContextConfiguration
//@AutoConfigureMockMvc
class AuthenticationControllerTest {

    @Autowired
    private WebApplicationContext context;
    @Autowired
    private AuthenticationController authenticationController;
    @Autowired
    private AuthenticationService authenticationService;

    private MockMvc mockMvc;

    private String token;

    private static boolean onceSetup = false;

    @Autowired
    private UserService userService;

    @BeforeEach
    void setUp() throws Exception {
        //Init MockMvc Object and build
       mockMvc = MockMvcBuilders
				.webAppContextSetup(context)
				.apply(springSecurity())
				.build();
//       if (!onceSetup) {
           var result = authenticationService.register(
                   RegisterRequest.builder().setEmail("admin").setPassword("password").setRole(Role.ADMIN).build()
           );
           token = result.getAccessToken();
//           onceSetup = true;
//       }
    }
    @AfterEach
    void tearDown() {
        userService.deleteUserByUsername(User.builder().setEmail("admin").build());
    }

    @Test
    void contextLoads() throws Exception {
        assertThat(authenticationController).isNotNull();
    }

    @Test
    void register() throws Exception {
        System.out.println("token: " + token);
        String body = """
                {
                  "role":"USER",
                  "password": "password",
                  "email": "shyam@mailing.com"
                }
                """;
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token)
                        .content(body))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void authenticate() throws Exception {
//        Todo fix this
        Thread.sleep(1000);
        String authenticateBody = """
                {
                    "email": "admin",
                    "password": "password"
                }
                """ ;
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authenticateBody))
                .andDo(print())
                .andExpect(status().isOk()).andReturn();
    }

}
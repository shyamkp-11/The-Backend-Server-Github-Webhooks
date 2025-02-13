package com.shyampatel.webapp.githubplayroom.githubuser;

import com.shyampatel.webapp.githubplayroom.TestConfig;
import com.shyampatel.webapp.githubplayroom.config.GithubPlayroomSecurityConfig;
import com.shyampatel.webapp.githubplayroom.config.JwtAuthenticationFilter;
import com.shyampatel.webapp.githubplayroom.config.JwtService;
import com.shyampatel.webapp.githubplayroom.token.TokenRepository;
import com.shyampatel.webapp.githubplayroom.user.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.io.IOException;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import({GithubPlayroomSecurityConfig.class, TestConfig.class})
@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = GithubUserController.class)
public class GithubUserControllerTest {

    @Autowired
    MockMvc mockMvc;
    @MockitoBean
    GithubUserService githubUserService;

    @BeforeEach
    public void setup() {
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void testSignedInToApp() throws Exception {

        Mockito.doReturn(new GithubUserResponseDto(
                "id",
                "deviceId",
                true
        )).when(githubUserService).signedInToApp(any());
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/githubUsers/signedInToApp")
                        .header("Authorization", "Bearer token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "fcmToken":"fcm_token",
                                    "deviceId":"device_id",
                                    "globalId":"globlal_id",
                                    "username":"shyam_kp",
                                    "firstName":"Shyam",
                                    "lastName":"Patel",
                                    "email":"shyam.patel@mail.com"
                                }
                                """)
                )
                .andDo(print()).
                andExpect(status().isOk()).andExpect(content()
                        .json("""
                                {
                                "globalId": "id",
                                "deviceId": "deviceId",
                                "fcmEnabled": true
                                }
                                """));
    }
}

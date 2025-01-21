package com.shyampatel.webapp.githubplayroom.auth;

import com.shyampatel.webapp.githubplayroom.config.JwtService;
import com.shyampatel.webapp.githubplayroom.token.TokenRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


//@SpringBootTest()
@WebMvcTest(controllers = AuthenticationController.class)
//@AutoConfigureMockMvc
class AuthenticationControllerTest {

    @Autowired
    private AuthenticationController authenticationController;

    @MockitoBean
    private AuthenticationService authenticationService;
    @MockitoBean
    private JwtService jwtService;
    @MockitoBean
    private TokenRepository tokenRepository;


    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    void setUp() {
        //Init MockMvc Object and build
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }
    @AfterEach
    void tearDown() {
    }

    @Test
    void contextLoads() throws Exception {
        assertThat(authenticationController).isNotNull();
    }

    @Test
    void register() throws Exception {
        Mockito.when(authenticationService.register(Mockito.any())).thenReturn(AuthenticationResponse.builder().build());
        assertThat(authenticationService.register(RegisterRequest.builder().build())).isEqualTo(AuthenticationResponse.builder().build());
        String requestBody = "{\"email\": \"\", \"password\": \"password\"}";
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void authenticate() {
    }

    @Test
    void refreshToken() {
    }
}
package com.acme.social.api;

import com.acme.social.config.SecurityConfig;
import com.acme.social.application.dto.RegisterResult;
import com.acme.social.application.usecases.RegisterUserUseCase;
import com.acme.social.infrastructure.db.AuthRepository;
import com.acme.social.security.jwt.JwtService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.context.annotation.Import;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AuthController.class)
@AutoConfigureMockMvc
@Import(SecurityConfig.class)
class AuthControllerRestIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthRepository authRepository;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private RegisterUserUseCase registerUserUseCase;

    @Test
    void registerShouldReturnCreated() throws Exception {
        UUID userId = UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");
        when(registerUserUseCase.execute(any())).thenReturn(new RegisterResult(userId, "ana_l"));

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "username": "ana",
                                  "password": "Ana12345!",
                                  "nombres": "Ana",
                                  "apellidos": "Lopez",
                                  "fechaNacimiento": "1995-04-12",
                                  "alias": "ana_l"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(userId.toString()))
                .andExpect(jsonPath("$.alias").value("ana_l"));
    }

    @Test
    void loginShouldReturnToken() throws Exception {
        UUID userId = UUID.fromString("11111111-1111-1111-1111-111111111111");
        String rawPassword = "Passw0rd!";
        String encodedPassword = new BCryptPasswordEncoder().encode(rawPassword);

        AuthRepository.UserAuthView user = new AuthRepository.UserAuthView(
                userId,
                "ana_l",
                "ana",
                encodedPassword
        );

        when(authRepository.findByUsername("ana")).thenReturn(Optional.of(user));
        when(jwtService.generateToken(
                ArgumentMatchers.eq(userId.toString()),
                ArgumentMatchers.eq("ana_l"),
                ArgumentMatchers.eq("ana"),
                ArgumentMatchers.any())
        ).thenReturn("test.jwt.token");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "username": "ana",
                                  "password": "Passw0rd!"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("test.jwt.token"))
                .andExpect(jsonPath("$.tokenType").value("Bearer"))
                .andExpect(jsonPath("$.expiresInSeconds").value(7200));

        verify(authRepository).findByUsername("ana");
    }
}

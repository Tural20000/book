package com.abdullayevtural.book.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;

import com.abdullayevtural.book.dto.AuthRequest;
import com.abdullayevtural.book.service.UserDetailsServiceImpl;
import com.abdullayevtural.book.utils.JwtUtil;
import com.abdullayevtural.book.utils.RefreshTokenUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(controllers = AuthController.class)
@DisplayName("AuthController Tests")
class AuthControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private AuthenticationManager authenticationManager;

	@MockBean
	private JwtUtil jwtUtil;

	@MockBean
	private UserDetailsServiceImpl userDetailsService;

	@MockBean
	private RefreshTokenUtil refreshTokenUtil;

	@Autowired
	private ObjectMapper objectMapper;

	private AuthRequest authRequest;
	private UserDetails userDetails;
	private String jwtToken;
	private String refreshToken;

	@BeforeEach
	void setUp() {
		authRequest = new AuthRequest();
		authRequest.setUsername("testuser");
		authRequest.setPassword("testpassword");

		userDetails = User.withUsername("testuser").password("encodedPassword")
				.authorities("ROLE_ADD", "ROLE_GET", "ROLE_UPDATE", "ROLE_DELETE").build();

		jwtToken = "test.jwt.token";
		refreshToken = "test.refresh.token";
	}

	@Test
	@DisplayName("POST /apis/login - Should return JWT and refresh token on successful authentication")
	void createAuthenticationToken_shouldReturnTokens_whenCredentialsAreValid() throws Exception {
		// Arrange
		when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(
				new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
		when(userDetailsService.loadUserByUsername(authRequest.getUsername())).thenReturn(userDetails);
		when(jwtUtil.generateToken(userDetails)).thenReturn(jwtToken);
		when(refreshTokenUtil.generateRefreshToken(userDetails)).thenReturn(refreshToken);

		// Act & Assert
		mockMvc.perform(post("/apis/login").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(authRequest))).andExpect(status().isOk())
				.andExpect(jsonPath("$.jwt").value(jwtToken)).andExpect(jsonPath("$.refreshToken").value(refreshToken));
	}

	@Test
	@DisplayName("POST /apis/login - Should return error on invalid credentials")
	void createAuthenticationToken_shouldReturnError_whenCredentialsAreInvalid() throws Exception {
		// Arrange
		when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
				.thenThrow(new BadCredentialsException("Bad credentials"));

		// Act & Assert
		mockMvc.perform(post("/apis/login").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(authRequest))).andExpect(status().isInternalServerError());
	}

	@Test
	@DisplayName("POST /apis/refresh-token - Should return new access token when refresh token is valid")
	void refreshToken_shouldReturnNewAccessToken_whenRefreshTokenIsValid() throws Exception {
		// Arrange
		String newAccessToken = "new.access.token";
		when(refreshTokenUtil.extractUsername(refreshToken)).thenReturn("testuser");
		when(userDetailsService.loadUserByUsername("testuser")).thenReturn(userDetails);
		when(refreshTokenUtil.validateToken(refreshToken, userDetails)).thenReturn(true);
		when(jwtUtil.generateToken(userDetails)).thenReturn(newAccessToken);

		// Act & Assert
		mockMvc.perform(post("/apis/refresh-token").contentType(MediaType.APPLICATION_JSON)
				.content("{\"refreshToken\":\"" + refreshToken + "\"}")).andExpect(status().isOk())
				.andExpect(jsonPath("$.jwt").value(newAccessToken))
				.andExpect(jsonPath("$.refreshToken").value(refreshToken));
	}

	@Test
	@DisplayName("POST /apis/refresh-token - Should return 403 when refresh token is invalid")
	void refreshToken_shouldReturn403_whenRefreshTokenIsInvalid() throws Exception {
		// Arrange
		when(refreshTokenUtil.extractUsername(refreshToken)).thenReturn("testuser");
		when(userDetailsService.loadUserByUsername("testuser")).thenReturn(userDetails);
		when(refreshTokenUtil.validateToken(refreshToken, userDetails)).thenReturn(false);

		// Act & Assert
		mockMvc.perform(post("/apis/refresh-token").contentType(MediaType.APPLICATION_JSON)
				.content("{\"refreshToken\":\"" + refreshToken + "\"}")).andExpect(status().isForbidden())
				.andExpect(content().string("Invalid refresh token"));
	}

	@Test
	@DisplayName("GET /apis/add - Should return 401 when not authenticated")
	void addData_shouldReturn401_whenNotAuthenticated() throws Exception {
		// Act & Assert - Without authentication, should return 401
		mockMvc.perform(get("/apis/add")).andExpect(status().isUnauthorized());
	}

	@Test
	@DisplayName("GET /apis/get - Should return 401 when not authenticated")
	void getData_shouldReturn401_whenNotAuthenticated() throws Exception {
		// Act & Assert - Without authentication, should return 401
		mockMvc.perform(get("/apis/get")).andExpect(status().isUnauthorized());
	}

	@Test
	@DisplayName("GET /apis/update - Should return 401 when not authenticated")
	void updateData_shouldReturn401_whenNotAuthenticated() throws Exception {
		// Act & Assert - Without authentication, should return 401
		mockMvc.perform(get("/apis/update")).andExpect(status().isUnauthorized());
	}

	@Test
	@DisplayName("GET /apis/delete - Should return 401 when not authenticated")
	void deleteData_shouldReturn401_whenNotAuthenticated() throws Exception {
		// Act & Assert - Without authentication, should return 401
		mockMvc.perform(get("/apis/delete")).andExpect(status().isUnauthorized());
	}
}

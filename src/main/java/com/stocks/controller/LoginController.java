package com.stocks.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.stocks.repository.UserRepository;
import com.stocks.security.ApiResponse;
import com.stocks.security.LoginRequest;
import com.stocks.security.LoginResponse;
import com.stocks.service.UserService;
import com.stocks.serviceImpl.UserServiceImpl;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
public class LoginController {

	@Autowired
	private UserService userService;

	@Autowired
	UserServiceImpl userServiceIml;

	@Autowired
	UserRepository userRepository;

	private static final String JWT_COOKIE_NAME = "jwtToken";

	@PostMapping(value = "/login/jwt")
	public ResponseEntity<ApiResponse<LoginResponse>> userLogin(@RequestBody LoginRequest loginRequest, HttpServletRequest request,
			HttpServletResponse response) {

		Boolean isPasswordExpired = userService.isPasswordOlderThan3Months(loginRequest.getEmail());

		if (isPasswordExpired) {

			return (ResponseEntity<ApiResponse<LoginResponse>>) ResponseEntity.status(333)
					.body(new ApiResponse<LoginResponse>("Failed", "user password is expired..!", null, 333));

		} else {

			log.info(loginRequest.toString());
			ResponseEntity<ApiResponse<LoginResponse>> userloginResponse = userService.generateToken(loginRequest);

			LoginResponse loginResponse = userloginResponse.getBody().getPayload();
			if (loginResponse != null) {
				  // **Create session**
	            HttpSession session = request.getSession(true);
	            session.setAttribute("loggedInUser", loginRequest.getEmail());
	            session.setMaxInactiveInterval(30 * 60); // **Session expires after 30 minutes**

				Cookie tokenCookie = new Cookie(JWT_COOKIE_NAME, loginResponse.getUserToken());
				tokenCookie.setMaxAge(24 * 60 * 60);
				tokenCookie.setPath("/");
				response.addCookie(tokenCookie);
				System.out.println(loginResponse.getLoginDate());

			}
			log.info("code is :" + userloginResponse.getStatusCode());
			return userloginResponse;
		}

	}
	
	
	@GetMapping("/logout")
	public ResponseEntity<ApiResponse<String>> logout(HttpServletRequest request, HttpServletResponse response) {
	    HttpSession session = request.getSession(false);

	    if (session != null) {
	        String user = (String) session.getAttribute("loggedInUser");
	        System.out.println("User logged out: " + user);
	        session.invalidate(); // **Invalidate session**
	        
	        // **Remove JWT token cookie**
	        Cookie tokenCookie = new Cookie(JWT_COOKIE_NAME, "");
	        tokenCookie.setMaxAge(0); // **Delete the cookie**
	        tokenCookie.setPath("/");
	        response.addCookie(tokenCookie);

	        return ResponseEntity.ok(new ApiResponse<>("SUCCESS", "User logged out successfully.", user, HttpStatus.OK.value()));
	    }

	    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
	            .body(new ApiResponse<>("FAILURE", "No active session found.", null, HttpStatus.BAD_REQUEST.value()));
	}

	@GetMapping("/access-denied")
	public ResponseEntity<ApiResponse<String>> accessDeniedPage() {
	    ApiResponse<String> response = new ApiResponse<>(
	        "error",
	        "You do not have permission to access this resource.",
	        null,
	        HttpStatus.FORBIDDEN.value()
	    );
	    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
	}
	
	@GetMapping("/session-check")
	public ResponseEntity<ApiResponse<Boolean>> checkSession(HttpServletRequest request) {
	    HttpSession session = request.getSession(false);
	    boolean isLoggedIn = (session != null && session.getAttribute("loggedInUser") != null);

	    return ResponseEntity.ok(new ApiResponse<>(
	            "SUCCESS",
	            isLoggedIn ? "Session is active" : "No active session",
	            isLoggedIn,
	            HttpStatus.OK.value()
	    ));
	}

}
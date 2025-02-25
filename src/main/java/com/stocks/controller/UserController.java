package com.stocks.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.stocks.entity.User;
import com.stocks.exception.ResourceNotFoundException;
import com.stocks.request.ResetPasswordRequest;
import com.stocks.security.ApiResponse;
import com.stocks.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Controller
@RequestMapping("/users")
public class UserController {

	private static final Logger logger = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private UserService userService;

	@GetMapping("/getAllUsers")
	@PreAuthorize("hasRole('ADMIN')  or hasRole('ROLE_USER_LIST')")
	public ResponseEntity<List<User>> getAllUsers(Model model) {
		return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);

	}

	@GetMapping("/{id}")
	public ResponseEntity<User> getUserById(@PathVariable int id) {
		User user = userService.getUserById(id);
		if (user != null) {
			return new ResponseEntity<>(user, HttpStatus.OK);
		} else {
			// You can customize the response based on your requirements
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@PostMapping("/createUser")
	public ResponseEntity<User> createUser(@RequestBody User user, HttpServletRequest request) {

		String url = request.getRequestURL().toString();
		url = url.replace(request.getServletPath(), "");
		User createUser = userService.createUser(user, url);

		if (createUser != null) {
			return new ResponseEntity<>(createUser, HttpStatus.OK);
		} else {
			// You can customize the response based on your requirements
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping("/update/{id}")
	public ResponseEntity<ApiResponse<User>> updateUser(@PathVariable int id, @RequestBody User updatedUser) {
		User user = userService.updateUser(id, updatedUser);
		ApiResponse<User> response = new ApiResponse<>("success", "User updated successfully", user,
				HttpStatus.OK.value());
		return ResponseEntity.ok(response);
	}

	@DeleteMapping("/{id}")
	public void deleteUser(@PathVariable int id) {
		userService.deleteUser(id);
	}

	@PostMapping("/reset-password")
	public ResponseEntity<ApiResponse<String>> resetPassword(@RequestBody ResetPasswordRequest request) {
		logger.info("Received API request to reset password for user: {}", request.getEmail());

		try {
			boolean isUpdated = userService.resetPassword(request.getCurrentPassword(), request.getEmail(),
					request.getNewPassword(), request.getConfNewPassword());

			ApiResponse<String> response = new ApiResponse<>("success", "Password reset successfully", null,
					HttpStatus.OK.value());

			logger.info("Password reset successfully for user: {}", request.getEmail());
			return ResponseEntity.ok(response);
		} catch (ResourceNotFoundException e) {
			logger.error("Error: {}", e.getMessage());
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(new ApiResponse<>("error", e.getMessage(), null, HttpStatus.NOT_FOUND.value()));
		} catch (IllegalArgumentException e) {
			logger.error("Validation Error: {}", e.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new ApiResponse<>("error", e.getMessage(), null, HttpStatus.BAD_REQUEST.value()));
		} catch (Exception e) {
			logger.error("Unexpected error occurred while resetting password for user: {}", request.getEmail(), e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
					new ApiResponse<>("error", "Something went wrong", null, HttpStatus.INTERNAL_SERVER_ERROR.value()));
		}
	}
}

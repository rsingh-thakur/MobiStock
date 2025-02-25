package com.stocks.service;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;

import com.stocks.entity.User;
import com.stocks.security.ApiResponse;
import com.stocks.security.LoginRequest;
import com.stocks.security.LoginResponse;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpSession;

public interface UserService {

	User getUserById(int id);

	User createUser(User user, String url);

	User updateUser(int id, User updatedUser);

	void deleteUser(int id);

	User loginUser(String email, String password);

	List<User> getAllUsers();

	String generateOtp(String email) throws MessagingException;

	void sendOtpEmail(String email, String otp) throws MessagingException;

	boolean verifyOtp(HttpSession session, String enteredOtp);

	Boolean updatePassword(long password, String currentUser);


	ResponseEntity<ApiResponse<LoginResponse>> generateToken(LoginRequest loginRequest);

	public void assignDefaultRoleToUser(String email);

	String getCurrentUser();
 
	Boolean isPasswordOlderThan3Months(String userEmail);

	  User findByEmail(String userEmail);

	boolean resetPassword(String currentPassword, String email, String newPassword, String confNewPassword);
}

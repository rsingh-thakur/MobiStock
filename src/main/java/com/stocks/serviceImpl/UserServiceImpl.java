package com.stocks.serviceImpl;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.stocks.entity.Role;
import com.stocks.entity.User;
import com.stocks.exception.DeactivatedUserException;
import com.stocks.exception.ResourceNotFoundException;
import com.stocks.exception.RoleAssignException;
import com.stocks.repository.RoleRepository;
import com.stocks.repository.UserRepository;
import com.stocks.security.ApiResponse;
import com.stocks.security.CustomUserDetails;
import com.stocks.security.CustomUserService;
import com.stocks.security.JwtUtil;
import com.stocks.security.LoginRequest;
import com.stocks.security.LoginResponse;
import com.stocks.service.UserService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private JavaMailSender javaMailSender;

	@Autowired
	private TemplateEngine templateEngine;

	@Autowired
	BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private CustomUserService userDetailsService;

	private final Map<String, String> otpCache = new HashMap<>();

	private static final Logger logger = LoggerFactory.getLogger(UserService.class);

	
	
	@Override
	public User getUserById(int id) {
		return userRepository.findById(id).orElse(null);
	}

	private final ScheduledExecutorService cleanupExecutor = Executors.newSingleThreadScheduledExecutor();

	public UserServiceImpl(UserRepository userRepository, JavaMailSender javaMailSender, TemplateEngine templateEngine,
			HttpServletResponse httpServletResponse) {
		super();
		this.userRepository = userRepository;
		this.javaMailSender = javaMailSender;
		this.templateEngine = templateEngine;

	}

	@Override
	public User createUser(User user, String url) {

		sendVerificationMail(user, url);

		Date date = Date.valueOf(new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime()));
		user.setPasswordUpdatedAt(date);
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		user.setStatus(1);

		User savedUser = userRepository.save(user);
		this.assignDefaultRoleToUser(savedUser.getEmail());
		return savedUser;
	}

	public User updateUser(int id, User updatedUser) {
		return userRepository.findById(id).map(existingUser -> {
			existingUser.setShopStoreName(updatedUser.getShopStoreName());
			existingUser.setShopAddress(updatedUser.getShopAddress());
			existingUser.setEmail(updatedUser.getEmail());
			existingUser.setPassword(updatedUser.getPassword());
			existingUser.setPasswordUpdatedAt(new Date(new java.util.Date().getTime())); // Update password timestamp
			existingUser.setRole(updatedUser.getRole());
			existingUser.setStatus(updatedUser.getStatus());
			return userRepository.save(existingUser);
		}).orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));
	}

	@Override
	public void assignDefaultRoleToUser(String email) {

		Role role;
		try {
			User user = userRepository.findByEmail(email);
			role = roleRepository.findByName("guest");
			user.setRole(role);
			roleRepository.save(role);
			userRepository.save(user);
			log.info("Default Role assign successsfully as Guest to the user ");
		} catch (Exception e) {
			log.error("failed to assign the role to user exception");
			throw new RoleAssignException("Failed to assign the default role Cause :" + e.getMessage());
		}

	}

	@Override
	public void deleteUser(int id) {
		userRepository.deleteById(id);
	}

	@Override
	public User loginUser(String email, String password) {
		return userRepository.findByEmailAndPassword(email, password);
	}

	public void sendVerificationMail(User user, String url) {

		String from = "yukisoft@gmail.com";
		String to = user.getEmail();
		String subject = "Welcome to Yuktisoft ";
		String content = "Dear [[name]],<br>" + "- Your Account has been Created at Yuktisoft Mobi app !<br>" + ""
				+ "<h3><a href=\"[[URL]]\" target=\"_self\">Login Here</a></h3>" + "Thank you,<br>";

		try {

			MimeMessage message = javaMailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message);

			helper.setFrom(from, "Yuktisoft.com");
			helper.setTo(to);
			helper.setSubject(subject);

			content = content.replace("[[name]]", user.getShopStoreName() + " " + user.getShopAddress());

			String siteUrl = url + "/users/loginUser";

			content = content.replace("[[URL]]", siteUrl);

			helper.setText(content, true);

			javaMailSender.send(message);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public List<User> getAllUsers() {
		return userRepository.findAll();

	}

	@Override
	public String generateOtp(String email) throws MessagingException {
		Random random = new Random();
		String otp = String.format("%04d", random.nextInt(10000));

		otpCache.put(email, otp);

		// Send the OTP via email
		sendOtpEmail(email, otp);

		cleanupExecutor.schedule(() -> otpCache.remove(email), 10, TimeUnit.MINUTES);

		return otp;
	}

	public void sendOtpEmail(String email, String otp) throws MessagingException {
		MimeMessage message = javaMailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true);

		Context context = new Context();
		context.setVariable("otp", otp);
		context.setVariable("email", email);
		String emailContent = templateEngine.process("forgot_password_template.html", context);
		helper.setTo(email);
		helper.setSubject("forgot password otp");
		helper.setText(emailContent, true);

		javaMailSender.send(message);

	}

	@Override
	public boolean verifyOtp(HttpSession session, String enteredOtp) {
		String storedOtp = otpCache.get(session.getAttribute("email"));
		return storedOtp != null && storedOtp.equals(enteredOtp);
	}

	@Override
	public Boolean updatePassword(long password, String currentUser) {
		User user = userRepository.findByEmail(currentUser);
		String pwd = String.valueOf(password);
		if (user != null) {

			user.setPassword(pwd);
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			Date date = Date.valueOf(new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime()));
			user.setPasswordUpdatedAt(date);
		}
		User savedUser = userRepository.save(user);
		if (savedUser != null)
			return true;
		else {
			return false;
		}
	}


    public boolean resetPassword(String currentPassword, String email, String newPassword, String confirmNewPassword) {
        logger.info("Password reset request received for user: {}", email);

        // Fetch user from DB
        User user = userRepository.findByEmail(email);

        // Case 1: Check if user exists
        if (user == null) {
            logger.warn("User not found with email: {}", email);
            throw new ResourceNotFoundException("User not found with email: " + email);
        }

        logger.debug("User found: {}", user.getEmail());

        // Case 2: Check if the current password matches the stored password
        boolean isCurrentPasswordCorrect = passwordEncoder.matches(currentPassword, user.getPassword());
        if (!isCurrentPasswordCorrect) {
            logger.warn("Incorrect current password entered for user: {}", email);
            throw new IllegalArgumentException("Current password is incorrect");
        }

        logger.debug("Current password matched successfully for user: {}", email);

        // Case 3: Check if new password and confirm password match
        if (!newPassword.equals(confirmNewPassword)) {
            logger.warn("New password and confirm password do not match for user: {}", email);
            throw new IllegalArgumentException("New password and confirm password do not match");
        }

        logger.info("New password validation passed for user: {}", email);

        // Update the password after validation
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setPasswordUpdatedAt(new Date(System.currentTimeMillis())); // Set update time

        // Save the user with the new password
        userRepository.save(user);

        logger.info("Password reset successful for user: {}", email);
        return true;
    }

	@Override
	public ResponseEntity<ApiResponse<LoginResponse>> generateToken(LoginRequest loginRequest) {
		String token = null;
		User user = null;

		log.info("request data : " + loginRequest.toString());
		try {
			this.authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

			CustomUserDetails userDetails = this.userDetailsService.loadUserByUsername(loginRequest.getEmail());
			token = jwtUtil.generateToken(userDetails);
			user = userRepository.findByEmail(loginRequest.getEmail());
			log.info("user found ....");
			return ResponseEntity.ok(new ApiResponse<LoginResponse>("success", "token fetched",
					new LoginResponse(token, user.getCreationDate()), 200));
		} catch (Exception e) {
			e.printStackTrace();
			log.error("exception thrown Exception handled ");
			if (e instanceof DeactivatedUserException) {
				log.error("DeactivatedUserException thrown DeactivatedUserException handled ");
				return ResponseEntity
						.ofNullable(new ApiResponse<LoginResponse>("Failed", "token not generated", null, 444));
			}

		}
		return ResponseEntity.ofNullable(new ApiResponse<LoginResponse>("Failed", "token not generated", null, 444));

	}

	@Override
	public String getCurrentUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String userEmail = null;
		if (authentication != null) {
			userEmail = authentication.getName();
		}
		return userEmail;
	}

	@Override
	public Boolean isPasswordOlderThan3Months(String userEmail) {
		User user = userRepository.findByEmail(userEmail);

		Date passwordUpdatedAt = user.getPasswordUpdatedAt();
		LocalDate threeMonthsAgo = LocalDate.now().minusMonths(3);

		System.out.println("user data checked ");

		if (passwordUpdatedAt != null && passwordUpdatedAt.toLocalDate().isBefore(threeMonthsAgo)) {
			// Password needs to be reset, send email
			return true;
		}
		return false;
	}

	@Override
	public User findByEmail(String userEmail) {
		return userRepository.findByEmail(userEmail);
	}

}

package ir.projectMohammadi.web.controller.authController;

import ir.projectMohammadi.model.enums.status.Status;
import ir.projectMohammadi.model.user.User;
import ir.projectMohammadi.service.otp.OtpService;
import ir.projectMohammadi.service.security.MyUserDetailsService;
import ir.projectMohammadi.service.user.IUserService;
import ir.projectMohammadi.util.ApiResponse;
import ir.projectMohammadi.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final IUserService userService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final OtpService otpService;
    private final MyUserDetailsService myUserDetailsService;
    private final JwtUtil jwtUtil;

    @PostMapping("/signup")
    @ResponseBody
    public ResponseEntity<ApiResponse> signup(@RequestBody User user) {
        if (userService.findByUsername(user.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, "Username already exists."));
        }
        if (userService.findByEmail(user.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, "Email already exists."));
        }
        if (userService.findByMobileNumber(user.getMobileNumber()).isPresent()) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, "Mobile number already exists."));
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setStatus(Status.PENDING);
        userService.save(user);

        return ResponseEntity.ok(new ApiResponse(true, "Signup request sent. Waiting for admin approval."));
    }

    @PostMapping("/login")
    @ResponseBody
    public ResponseEntity<ApiResponse> login(@RequestParam String username, @RequestParam String password) {
        Optional<User> userOptional = userService.findByUsername(username);
        if (userOptional.isEmpty()) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, "User not found."));
        }

        User user = userOptional.get();

        if (user.getStatus() == Status.PENDING) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, "Your account is pending approval. Please wait for admin verification."));
        }

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (BadCredentialsException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, "Invalid username or password."));
        }

        final UserDetails userDetails = myUserDetailsService.loadUserByUsername(username);
        final String jwt = jwtUtil.generateToken(userDetails);

        return ResponseEntity.ok(new ApiResponse(true, "Login successful.", jwt));
    }

    @PostMapping("/forgot-password")
    @ResponseBody
    public ResponseEntity<ApiResponse> forgotPassword(@RequestBody Map<String, String> request) {
        String identifier = request.get("Email");
        Optional<User> user = userService.findByEmail(identifier);
        if (user.isEmpty()) {
            user = userService.findByMobileNumber(identifier);
        }
        if (user.isEmpty()) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, "User not found with given email or phone number."));
        }
        String otp = otpService.generateOtp(identifier);
        return ResponseEntity.ok(new ApiResponse(true, "OTP sent successfully.", otp));
    }

    @PostMapping("/verify-otp")
    @ResponseBody
    public ResponseEntity<ApiResponse> verifyOtp(@RequestBody Map<String, String> request) {
        String identifier = request.get("Email");
        String otp = request.get("otp");

        boolean isValid = otpService.validateOtp(identifier, otp);
        if (!isValid) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, "Invalid OTP."));
        }

        otpService.removeOtp(identifier);
        return ResponseEntity.ok(new ApiResponse(true, "OTP verified. You can now reset your password."));
    }

    @PostMapping("/reset-password")
    @ResponseBody
    public ResponseEntity<ApiResponse> resetPassword(@RequestBody Map<String, String> request) {
        String identifier = request.get("Email");
        String newPassword = request.get("newPassword");

        Optional<User> user = userService.findByEmail(identifier);
        if (user.isEmpty()) {
            user = userService.findByMobileNumber(identifier);
        }

        if (user.isEmpty()) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, "User not found."));
        }

        user.get().setPassword(passwordEncoder.encode(newPassword));
        userService.save(user.get());

        return ResponseEntity.ok(new ApiResponse(true, "Password reset successfully."));
    }

    /**
     * هندل کردن تمامی استثناها برای ارسال خروجی JSON
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> handleException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(false, "An error occurred: " + ex.getMessage()));
    }
}

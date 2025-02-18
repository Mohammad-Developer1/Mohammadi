package ir.projectMohammadi.web.controller.authController;

import ir.projectMohammadi.model.enums.status.Status;
import ir.projectMohammadi.model.user.User;
import ir.projectMohammadi.service.otp.OtpService;
import ir.projectMohammadi.service.security.MyUserDetailsService;
import ir.projectMohammadi.service.user.IUserService;
import ir.projectMohammadi.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
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
public class AuthController {

    @Autowired
    private IUserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private OtpService otpService;

    @Autowired
    private MyUserDetailsService myUserDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/signup")
    @ResponseBody
    public ResponseEntity<String> signup(@RequestBody User user) {

        if (userService.findByUsername(user.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("Username already exists.");
        }
        if (userService.findByEmail(user.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("Email already exists.");
        }
        if (userService.findByMobileNumber(user.getMobileNumber()).isPresent()) {
            return ResponseEntity.badRequest().body("Mobile number already exists.");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setStatus(Status.PENDING);
        userService.save(user);

        return ResponseEntity.ok("Signup request sent. Waiting for admin approval.");
    }

    @PostMapping("/login")
    @ResponseBody
    public ResponseEntity<?> login(@RequestParam String username, @RequestParam String password) {
        Optional<User> userOptional = userService.findByUsername(username);
        if (userOptional.isEmpty()) {
            return ResponseEntity.badRequest().body("User not found.");
        }

        User user = userOptional.get();

        if (user.getStatus() == Status.PENDING) {
            return ResponseEntity.badRequest().body("Your account is pending approval. Please wait for admin verification.");
        }

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (BadCredentialsException e) {
            return ResponseEntity.badRequest().body("Invalid username or password.");
        }

        final UserDetails userDetails = myUserDetailsService.loadUserByUsername(username);
        final String jwt = jwtUtil.generateToken(userDetails);

        return ResponseEntity.ok(jwt);
    }

    @PostMapping("/forgot-password")
    @ResponseBody
    public ResponseEntity<String> forgotPassword(@RequestBody Map<String, String> request) {
        String identifier = request.get("identifier");
        Optional<User> user = userService.findByEmail(identifier);
        if (user.isEmpty()) {
            user = userService.findByMobileNumber(identifier);
        }
        if (user.isEmpty()) {
            return ResponseEntity.badRequest().body("User not found with given email or phone number.");
        }
        String otp = otpService.generateOtp(identifier);
        return ResponseEntity.ok("OTP sent successfully: " + otp);
    }

    @PostMapping("/verify-otp")
    @ResponseBody
    public ResponseEntity<String> verifyOtp(@RequestBody Map<String, String> request) {
        String identifier = request.get("identifier");
        String otp = request.get("otp");

        boolean isValid = otpService.validateOtp(identifier, otp);
        if (!isValid) {
            return ResponseEntity.badRequest().body("Invalid OTP.");
        }

        otpService.removeOtp(identifier);
        return ResponseEntity.ok("OTP verified. You can now reset your password.");
    }


    @PostMapping("/reset-password")
    @ResponseBody
    public ResponseEntity<String> resetPassword(@RequestBody Map<String, String> request) {
        String identifier = request.get("identifier");
        String newPassword = request.get("newPassword");

        Optional<User> user = userService.findByEmail(identifier);
        if (user.isEmpty()) {
            user = userService.findByMobileNumber(identifier);
        }

        if (user.isEmpty()) {
            return ResponseEntity.badRequest().body("User not found.");
        }

        user.get().setPassword(passwordEncoder.encode(newPassword));
        userService.save(user.get());

        return ResponseEntity.ok("Password reset successfully.");
    }



}

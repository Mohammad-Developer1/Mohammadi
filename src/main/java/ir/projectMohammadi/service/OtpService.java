package ir.projectMohammadi.service;



import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Random;

@Service
public class OtpService {

    private final HashMap<String, String> otpStorage = new HashMap<>();
    private final Random random = new Random();

    public String generateOtp(String mobileNumber) {
        String otp = String.valueOf(100000 + random.nextInt(900000)); 
        otpStorage.put(mobileNumber, otp);
        return otp;
    }

    public boolean validateOtp(String mobileNumber, String otp) {
        return otpStorage.containsKey(mobileNumber) && otpStorage.get(mobileNumber).equals(otp);
    }

    public void removeOtp(String mobileNumber) {
        otpStorage.remove(mobileNumber);
    }
}

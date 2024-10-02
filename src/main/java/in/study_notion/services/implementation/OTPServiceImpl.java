package in.study_notion.services.implementation;

import in.study_notion.exceptions.GeneralExceptionHandler;
import in.study_notion.exceptions.OtpCreationException;
import in.study_notion.exceptions.UserAlreadyExistsException;
import in.study_notion.models.OTP;
import in.study_notion.models.User;
import in.study_notion.repositories.OTPRepository;
import in.study_notion.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.security.SecureRandom;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class OTPServiceImpl {

    private final UserRepository userRepository;
    private final OTPRepository otpRepository;
    private final EmailSenderService emailSenderService;
    private static final SecureRandom secureRandom = new SecureRandom();
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    @Autowired
    public OTPServiceImpl(UserRepository userRepository, OTPRepository otpRepository, EmailSenderService emailSenderService) {
        this.userRepository = userRepository;
        this.otpRepository = otpRepository;
        this.emailSenderService = emailSenderService;
    }


    @Transactional
    public OTP generateOtp(String email) {
        try {
            // Check if the user exists
            User user = this.userRepository.findByEmail(email);

            // Throw exception if user is already present
            if (user != null) {
                throw new UserAlreadyExistsException(String.format("%s this email already present in the application please try another different email", email));
            }

            // Check if an OTP exists for the given email
            OTP existingOtp = this.otpRepository.findByEmail(email);
            if (existingOtp != null) {
                // Check if the existing OTP is still valid (not expired)
                LocalDateTime expiryTime = existingOtp.getCreatedAt().plusMinutes(2);
                if (LocalDateTime.now().isBefore(expiryTime)) {
                    throw new OtpCreationException("An OTP has already been generated for this email. Please wait for the current OTP to expire.",null);
                }
                // If the OTP has expired, you can delete it (optional)
                otpRepository.delete(existingOtp);
                log.info("Expired OTP for email: {}, deleted it", email);
            }

            // Generate OTP
            String otp = generateUniqueOtp();
            log.info("Generated otp {} to the user {}", otp, email);

            OTP otpObj = new OTP();
            otpObj.setOtp(otp);
            otpObj.setEmail(email);
            otpObj.setCreatedAt(LocalDateTime.now());

            // Save the OTP
            OTP otpObject = this.otpRepository.save(otpObj);
            log.info("Saved otp {} ", otpObject.getOtp());

            String message = String.format("StudyNotion Authentication OTP %s Do not share otp ", otp);
            executor.submit(() -> this.emailSenderService.sendMail(email, "OTP VERIFICATION EMAIL BY STUDY NOTION EDUCATION PLATFORM", message));

            // Schedule the deletion of the OTP after 2 minutes
            scheduler.schedule(() -> {
                otpRepository.deleteByEmail(email); // Delete OTP by email
                log.info("Deleted OTP for email: {}", email);
            }, 2, TimeUnit.MINUTES);

            return otpObject;

        } catch (GeneralExceptionHandler e) {
            log.error("OTP Creation exception: {}", e.getMessage());
            // Re-throw your custom exception without using a generic one
            throw new OtpCreationException("Failed to create OTP due to an internal error", e);
        }
    }

    private  String generateUniqueOtp() {
            //generate otp in 6 digit form
            int randomOtp = 100000 + secureRandom.nextInt(900000);

            // add current time so that achieve uniqueness
            long currentTimeMillis = Instant.now().toEpochMilli();

            // create unique OTP by combining random number and timestamp
            return String.format("%06d", Math.abs((randomOtp + (int) currentTimeMillis) % 1000000));
        }

    public OTP findByEmail(String email){

        /*
        This method is used for find email
         */
        return this.otpRepository.findByEmail(email);
    }
}

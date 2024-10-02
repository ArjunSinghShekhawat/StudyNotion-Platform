package in.study_notion.controllers;

import in.study_notion.dependency.AuthDependencies;
import in.study_notion.exceptions.OtpCreationException;
import in.study_notion.models.OTP;
import in.study_notion.models.Profile;
import in.study_notion.models.User;
import in.study_notion.repositories.OTPRepository;
import in.study_notion.request.LoginRequest;
import in.study_notion.request.OTPRequest;
import in.study_notion.request.SignUpRequest;
import in.study_notion.responce.AuthResponce;
import in.study_notion.services.implementation.CustomeUserDetailsService;
import in.study_notion.services.implementation.OTPServiceImpl;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/auth")
public class Auth {

    private final AuthDependencies authDependencies;
    private final AuthenticationManager authenticationManager;
    private final CustomeUserDetailsService userDetailsService;
    private final OTPRepository otpRepository;
    private final OTPServiceImpl otpServiceImpl;

    @Autowired
    public Auth(AuthDependencies authDependencies,
                AuthenticationManager authenticationManager,
                CustomeUserDetailsService userDetailsService,
                OTPRepository otpRepository,
                OTPServiceImpl otpServiceImpl) {
        this.authDependencies = authDependencies;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.otpRepository = otpRepository;
        this.otpServiceImpl = otpServiceImpl;
    }

    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @PostMapping("/otp")
    public String getOtp(@Valid @RequestBody OTPRequest otpRequest) throws OtpCreationException {
        OTP otp = this.otpServiceImpl.generateOtp(otpRequest.getEmail());
        return otp.getOtp();
    }

    @Transactional
    @PostMapping("/signup")
    public ResponseEntity<AuthResponce> signup(@Valid @RequestBody SignUpRequest user) throws OtpCreationException {

        try{
            // Check if the user already exists
            User existsUser = this.authDependencies.getUserRepository().findByEmail(user.getEmail());

            if (existsUser != null) {
                AuthResponce response = new AuthResponce();
                response.setStatus(false);
                response.setMessage(String.format("%s already exists with %s. Please try another email", existsUser.getFirstName(), existsUser.getEmail()));
                response.setJwt(null);
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }

            //otp validation
            Optional<OTP> otpObj = this.otpRepository.findFirstByEmailOrderByCreatedAtDesc(user.getEmail());

            if (otpObj.isEmpty() || otpObj.get().getOtp().isEmpty() || !otpObj.get().getOtp().equals(user.getOtp())) {
                throw new OtpCreationException("OTP validation error", null);
            }

            log.info("Otp verification complete successful");

            // Create new user
            User newUser = new User();
            newUser.setFirstName(user.getFirstName());
            newUser.setLastName(user.getLastName());
            newUser.setPassword(passwordEncoder.encode(user.getPassword()));
            newUser.setCreatedAt(LocalDateTime.now());
            newUser.setEmail(user.getEmail());
            newUser.setPhoneNumber(user.getPhoneNumber());
            newUser.setAccountTypes(user.getAccountTypes());
            newUser.setImageUrl(String.format("https://api.dicebear.com/5.x/initials/svg?seed=%s %s", user.getFirstName(), user.getLastName()));

            // Create profile
            Profile profile = new Profile();
            profile.setAbout(null);
            profile.setGender(null);
            profile.setDateOfBirth(null);

            //save profile
            Profile savedProfile = this.authDependencies.getProfileRepository().save(profile);
            newUser.setProfile(savedProfile);

            // Save user
            User savedUser = this.authDependencies.getUserRepository().save(newUser);

            log.info("user sign up successful");

            //send response
            AuthResponce response = new AuthResponce();
            response.setStatus(true);
            response.setMessage(String.format("%s you have successfully signed up on the best education platform Study Notion", savedUser.getFirstName()));
            response.setJwt(null);

            return new ResponseEntity<>(response, HttpStatus.CREATED);
        }catch (Exception e){
            log.error("Error Occurred while signup a new user !");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponce> login(@RequestBody LoginRequest user) {
        try {
            log.info("Enter in login section !");
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));
            UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
            String jwt = this.authDependencies.getJwtUtils().generateToken(userDetails.getUsername());

            AuthResponce authResponce = new AuthResponce();
            authResponce.setMessage("Login successful");
            authResponce.setStatus(true);
            authResponce.setJwt(jwt);

            log.info("User {} login successful",userDetails.getUsername());

            return new ResponseEntity<>(authResponce, HttpStatus.OK);
        } catch (Exception e) {
            AuthResponce authResponce = new AuthResponce();
            authResponce.setMessage("Something went wrong");
            authResponce.setStatus(false);
            authResponce.setJwt(null);
            log.error("Exception occurred while creating authentication token", e);
            return new ResponseEntity<>(authResponce, HttpStatus.BAD_REQUEST);
        }
    }
}

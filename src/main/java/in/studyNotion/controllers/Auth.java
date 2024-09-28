package in.studyNotion.controllers;

import in.studyNotion.models.Profile;
import in.studyNotion.models.User;
import in.studyNotion.repositories.ProfileRepository;
import in.studyNotion.repositories.UserRepository;
import in.studyNotion.request.LoginRequest;
import in.studyNotion.request.SignUpRequest;
import in.studyNotion.responce.AuthResponce;
import in.studyNotion.services.implement.CustomeUserDetailsService;
import in.studyNotion.utils.JwtUtils;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
@Slf4j
@RestController
@RequestMapping("/auth")
public class Auth {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CustomeUserDetailsService userDetailsService;


    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private JwtUtils jwtUtils;


    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Transactional
    @PostMapping("/signup")
    public ResponseEntity<AuthResponce>signup(@Valid @RequestBody SignUpRequest user) {

        //if check this user already exists or not
        User existsUser =  this.userRepository.findByEmail(user.getEmail());

        if(existsUser!=null){
            AuthResponce response = new AuthResponce();

            response.setStatus(false);
            response.setMessage(String.format("%s already exists with %s Please try another email",existsUser.getFirstName(),existsUser.getEmail()));
            response.setJwt(null);

            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        //if this user is not present already then sign up successfully
        User newUser = new User();

        newUser.setFirstName(user.getFirstName());
        newUser.setLastName(user.getLastName());
        newUser.setPassword(passwordEncoder.encode(user.getPassword()));
        newUser.setCreatedAt(LocalDateTime.now());
        newUser.setEmail(user.getEmail());
        newUser.setPhoneNumber(user.getPhoneNumber());
        newUser.setAccountTypes(user.getAccountTypes());

        //create profile
        Profile profile = new Profile();

        profile.setAbout(null);
        profile.setGender(null);
        profile.setDateOfBirth(null);

        Profile savedProfile = this.profileRepository.save(profile);
        newUser.setProfile(savedProfile);

        //saved user
        User savedUser = this.userRepository.save(newUser);

        AuthResponce response = new AuthResponce();

        response.setStatus(true);
        response.setMessage(String.format("%s you are successfully sign up in india best education platform study notion",savedUser.getFirstName()));
        response.setJwt(null);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponce>login(@RequestBody LoginRequest user){

       try{
           authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(),user.getPassword()));
           UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
           String jwt = jwtUtils.generateToken(userDetails.getUsername());

           AuthResponce authResponce = new AuthResponce();
           authResponce.setMessage("login successfully ");
           authResponce.setStatus(true);
           authResponce.setJwt(jwt);

           return new ResponseEntity<>(authResponce,HttpStatus.OK);
       }catch (Exception e){

           AuthResponce authResponce = new AuthResponce();
           authResponce.setMessage("Something went wrong");
           authResponce.setStatus(false);
           authResponce.setJwt(null);

           log.error("Exception occurred while createAuthenticationToken ", e);
           return new ResponseEntity<>(authResponce, HttpStatus.BAD_REQUEST);
       }

    }

}

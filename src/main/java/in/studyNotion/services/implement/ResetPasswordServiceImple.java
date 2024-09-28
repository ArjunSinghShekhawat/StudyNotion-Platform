package in.studyNotion.services.implement;

import in.studyNotion.models.User;
import in.studyNotion.repositories.UserRepository;
import in.studyNotion.utils.GenerateToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Slf4j
@Service
public class ResetPasswordServiceImple {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailSenderService emailSenderService;

    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Transactional
    public boolean resetPasswordTokenGenerate(String email) throws Exception {

        //find this user present or not in database
        User existsUser = this.userRepository.findByEmail(email);

        if(existsUser==null){
            throw new Exception("User Not Present Please signup firstly");
        }

        //token creation
        String token = GenerateToken.generateToken();

        existsUser.setToken(token);
        existsUser.setResetPasswordExpire(LocalTime.now().plusHours(1));

        User updatedUser = this.userRepository.save(existsUser);

        String url = "http://localhost:3000/update-password/"+token;
        String message = String.format("Your Link for email verification is %s Please click this url to reset your password.",url);

        return this.emailSenderService.sendMail(email, "Reset Password", message);

    }
    @Transactional
    public String resetPassword(String password,String confirmPassword,String token){

        if(!password.equals(confirmPassword)){
            return "Password and Confirm Password Does not Match";
        }
        User existsUser = this.userRepository.findByToken(token);

        if(existsUser==null){
            return "Token is invalid";
        }

        if (!(existsUser.getResetPasswordExpire().isAfter(LocalTime.now()))) {
            return "Token is Expired, Please Regenerate Your Token";
        }
        //update password
        existsUser.setPassword(passwordEncoder.encode(password));

        this.userRepository.save(existsUser);

        String message = String.format("Your updated password is %s -----> ",confirmPassword);

        this.emailSenderService.sendMail(existsUser.getEmail(), "Reset Password Successfully ",message);

        return "Reset Password Successfully !";
    }


}

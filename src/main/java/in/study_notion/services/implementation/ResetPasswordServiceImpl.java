package in.study_notion.services.implementation;

import in.study_notion.models.User;
import in.study_notion.repositories.UserRepository;
import in.study_notion.utils.GenerateToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class ResetPasswordServiceImpl {

    private final UserRepository userRepository;
    private final EmailSenderService emailSenderService;

    @Autowired
    public ResetPasswordServiceImpl(UserRepository userRepository, EmailSenderService emailSenderService) {
        this.userRepository = userRepository;
        this.emailSenderService = emailSenderService;
    }
    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private static final ExecutorService executer = Executors.newScheduledThreadPool(2);


    @Transactional
    public boolean resetPasswordTokenGenerate(String email)  {

        boolean isSend = false;
       try{
           //find this user present or not in database
           User existsUser = this.userRepository.findByEmail(email);

           if(existsUser==null){
               throw new NoSuchFieldException("User Not Present Please signup firstly");
           }

           //token creation
           String token = GenerateToken.generateToken();

           existsUser.setToken(token);
           existsUser.setResetPasswordExpire(LocalTime.now().plusHours(1));

           this.userRepository.save(existsUser);

           String url = "http://localhost:3000/update-password/"+token;
           String message = String.format("Your Link for email verification is %s Please click this url to reset your password.",url);

           executer.submit(()->this.emailSenderService.sendMail(email, "Reset Password", message));

       }catch (NoSuchFieldException e){
           log.error("Error occurred while create reset password token {} ",e.getMessage());
       }
       return isSend;
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

        executer.submit(()->this.emailSenderService.sendMail(existsUser.getEmail(), "Reset Password Successfully ",message));
        shutdownScheduler();

        return "Reset Password Successfully !";
    }
    private void shutdownScheduler() {
        executer.shutdown();
        try {
            if (!executer.awaitTermination(60, TimeUnit.SECONDS)) {
                executer.shutdownNow();
            }
        } catch (InterruptedException e) {
            executer.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}

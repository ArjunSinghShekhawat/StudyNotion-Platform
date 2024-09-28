package in.studyNotion.controllers;

import in.studyNotion.models.User;
import in.studyNotion.repositories.UserRepository;
import in.studyNotion.request.ResetPasswordRequest;
import in.studyNotion.responce.UpdatedUserResponce;
import in.studyNotion.services.implement.ResetPasswordServiceImple;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/reset-password")
public class ResetPasswordController {

    @Autowired
    private ResetPasswordServiceImple resetPasswordServiceImple;

    @Autowired
    private UserRepository userRepository;


    @GetMapping("/get-link")
    public ResponseEntity<Boolean> resetPasswordTokenSend(@RequestBody ResetPasswordRequest resetPasswordRequest){
        try{
            boolean sendToken = this.resetPasswordServiceImple.resetPasswordTokenGenerate(resetPasswordRequest.getEmail());
            return new ResponseEntity<>(sendToken, HttpStatus.OK);

        }catch (Exception e){
            log.error("Error Occurred while send password token link {} ",e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    @PutMapping("/updated")
    public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordRequest resetPasswordRequest){
        try{

            User user = this.userRepository.findByEmail(resetPasswordRequest.getEmail());
            String message = this.resetPasswordServiceImple.resetPassword(resetPasswordRequest.getPassword(), resetPasswordRequest.getConfirmPassword(), user.getToken());

            return new ResponseEntity<>(message, HttpStatus.OK);

        }catch (Exception e){
            log.error("Error Occurred while reset password {} ",e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}

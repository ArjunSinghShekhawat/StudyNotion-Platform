package in.study_notion.controllers;

import in.study_notion.models.User;
import in.study_notion.repositories.UserRepository;
import in.study_notion.request.ResetPasswordRequest;
import in.study_notion.services.implementation.ResetPasswordServiceImpl;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/reset-password")
public class ResetPasswordController {

    private final ResetPasswordServiceImpl resetPasswordServiceImple;
    private final UserRepository userRepository;

    @Autowired
    public ResetPasswordController(ResetPasswordServiceImpl resetPasswordServiceImple, UserRepository userRepository) {
        this.resetPasswordServiceImple = resetPasswordServiceImple;
        this.userRepository = userRepository;
    }

    @GetMapping("/get-link")
    public ResponseEntity<Boolean> resetPasswordTokenSend(@RequestBody ResetPasswordRequest resetPasswordRequest){
        try{
            boolean sendToken = this.resetPasswordServiceImple.resetPasswordTokenGenerate(resetPasswordRequest.getEmail());

            if(sendToken){
                return new ResponseEntity<>(true, HttpStatus.OK);
            }
            return new ResponseEntity<>(false,HttpStatus.NO_CONTENT);

        }catch (Exception e){
            log.error("Error Occurred while send password token link {} ",e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    @PutMapping("/updated")
    public ResponseEntity<String> resetPassword(@Valid @RequestBody ResetPasswordRequest resetPasswordRequest){
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

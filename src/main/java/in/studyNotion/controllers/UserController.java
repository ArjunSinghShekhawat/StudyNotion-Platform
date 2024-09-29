package in.studyNotion.controllers;

import in.studyNotion.domain.ProfileDto;
import in.studyNotion.models.Profile;
import in.studyNotion.models.User;
import in.studyNotion.request.UserRequest;
import in.studyNotion.responce.UpdatedUserResponce;
import in.studyNotion.services.ProfileService;
import in.studyNotion.services.UserService;
import in.studyNotion.services.implement.UserServiceImple;
import in.studyNotion.utils.JwtUtils;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/student")
@Slf4j
public class UserController {

    @Autowired
    private ProfileService profileService;


    @Autowired
    private JwtUtils jwtUtils;


    @Autowired
    private UserService userService;


    @PutMapping("/profile-update")
    public ResponseEntity<Boolean>updateProfile(@Valid @RequestBody ProfileDto profileDto,@RequestHeader("Authorization") String jwt){
        try{
            boolean updated = this.profileService.updateProfile(profileDto, jwt);
            return new ResponseEntity<>(updated, HttpStatus.OK);

        }catch (Exception e){
            log.error("Error occur while update user profile "+e.getMessage());
            return new ResponseEntity<>(false,HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/profile-get")
    public ResponseEntity<?>getProfile(@RequestHeader("Authorization") String jwt){
        try{
            Profile userProfile = this.profileService.getUserProfile(jwt);

            if(userProfile!=null){
                return new ResponseEntity<>(userProfile, HttpStatus.OK);
            }
        }catch (Exception e){
            log.error("Error occur while get user profile "+e.getMessage());
        }
        return new ResponseEntity<>("User Profile Not Found ",HttpStatus.NOT_FOUND);
    }
    @PutMapping("/information-update")
    public ResponseEntity<UpdatedUserResponce>updateUserInformation(@RequestHeader("Authorization") String jwt, @RequestBody UserRequest user){
        try{
            User updatedUser = this.userService.updateUser(jwt, user);

            UpdatedUserResponce response = new UpdatedUserResponce();

            response.setFirstName(updatedUser.getFirstName());
            response.setLastName(updatedUser.getLastName());

            return new ResponseEntity<>(response,HttpStatus.OK);

        }catch (Exception e){
            log.error("Error occur while update user"+e.getMessage());
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}

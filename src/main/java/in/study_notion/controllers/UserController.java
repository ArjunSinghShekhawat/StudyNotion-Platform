package in.study_notion.controllers;

import in.study_notion.domain.ProfileDto;
import in.study_notion.models.Profile;
import in.study_notion.models.User;
import in.study_notion.request.UserRequest;
import in.study_notion.responce.UpdatedUserResponce;
import in.study_notion.services.ProfileService;
import in.study_notion.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    private final ProfileService profileService;
    private final UserService userService;

    @Autowired
    public UserController(ProfileService profileService,UserService userService){
        this.userService=userService;
        this.profileService=profileService;
    }

    @PutMapping("/profile-update")
    public ResponseEntity<Boolean>updateProfile(@RequestBody ProfileDto profileDto,@RequestHeader("Authorization") String jwt){
        try{
            boolean updated = this.profileService.updateProfile(profileDto, jwt);

            if(updated){
                return new ResponseEntity<>(true, HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        }catch (Exception e){
            log.error("Error occurred while update user profile {}", e.getMessage());
            return new ResponseEntity<>(false,HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/profile-get")
    public ResponseEntity<Profile>getProfile(@RequestHeader("Authorization") String jwt){
        try{
            Profile userProfile = this.profileService.getUserProfile(jwt);

            if(userProfile!=null){
                return new ResponseEntity<>(userProfile, HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        }catch (Exception e){
            log.error("Error occur while get user profile {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/information-update")
    public ResponseEntity<UpdatedUserResponce>updateUserInformation(@RequestHeader("Authorization") String jwt,
                                                                    @RequestBody UserRequest user){
        try{
            User updatedUser = this.userService.updateUser(jwt, user);

            if(updatedUser!=null){

                UpdatedUserResponce response = new UpdatedUserResponce();

                response.setFirstName(updatedUser.getFirstName());
                response.setLastName(updatedUser.getLastName());

                return new ResponseEntity<>(response,HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        }catch (Exception e){
            log.error("Error occur while update user{}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/get-user-info")
    public ResponseEntity<User>getUserInformation(@RequestHeader("Authorization") String jwt){

       try{
           User userInformation = this.userService.getUserAllInformationByEmail(jwt);
           if(userInformation!=null){
               return new ResponseEntity<>(userInformation,HttpStatus.OK);
           }
           return new ResponseEntity<>(HttpStatus.NOT_FOUND);

       }catch (Exception e){
           log.error("Error occurred while get user information");
           return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
       }
    }
}

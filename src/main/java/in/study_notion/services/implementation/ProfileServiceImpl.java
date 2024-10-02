package in.study_notion.services.implementation;

import in.study_notion.domain.ProfileDto;
import in.study_notion.models.Profile;
import in.study_notion.models.User;
import in.study_notion.repositories.ProfileRepository;
import in.study_notion.repositories.UserRepository;
import in.study_notion.services.ProfileService;
import in.study_notion.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Optional;

@Slf4j
@Service
public class ProfileServiceImpl implements ProfileService {

    
    private final ProfileRepository profileRepository;
    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;
    
    @Autowired
    public ProfileServiceImpl(ProfileRepository profileRepository, UserRepository userRepository, JwtUtils jwtUtils){
        
        this.jwtUtils=jwtUtils;
        this.profileRepository=profileRepository;
        this.userRepository=userRepository;
    }
    
    
    @Transactional
    @Override
    public boolean updateProfile(ProfileDto profileDto,String jwt) {

        /*
            This function is used to update user profile information
        */

        boolean updated = false;
        try{

            //fetch user information by jwt token
            User user = fetchUserInformationByJwtToken(jwt);

            //fetch user profile id
            ObjectId profileId = user.getProfile().getId();

            //fetch user profile
            Optional<Profile> existsProfile = this.profileRepository.findById(profileId);

            //check user profile exist or not
            if(existsProfile.isPresent()){

                Profile existProfile = existsProfile.orElseThrow(() -> new NoSuchElementException("Profile not found"));

                Profile profile = getProfile(profileDto, existProfile);
                this.profileRepository.save(profile);

                updated=true;
            }
            return updated;
        }catch (Exception e){
            log.error("Error occurred while update user profile {}", e.getMessage());
            return updated;
        }
    }

    private  Profile getProfile(ProfileDto profileDto, Profile profile) {


        //update profile information
        profile.setAbout(profileDto.getAbout() != null && !profileDto.getAbout().isEmpty() ? profileDto.getAbout() : profile.getAbout());
        profile.setGender(profileDto.getGender() != null ? profileDto.getGender() : profile.getGender());
        profile.setDateOfBirth(profileDto.getDateOfBirth() != null && !profileDto.getDateOfBirth().isEmpty() ? profileDto.getDateOfBirth() : profile.getDateOfBirth());

        return profile;
    }

    @Override
    public Profile getUserProfile(String jwt) {

        /*
           This Method is used to fetch the user profile base on user details
        */

        User user = fetchUserInformationByJwtToken(jwt);
        return user.getProfile();
    }

    private User fetchUserInformationByJwtToken(String jwt){

        /*
           this function is used to fetch the user details by jwt token
         */

        String userEmail  = jwtUtils.getEmailFromToken(jwt);
        return this.userRepository.findByEmail(userEmail);
    }


}

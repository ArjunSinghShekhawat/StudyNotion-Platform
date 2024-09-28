package in.studyNotion.services.implement;

import in.studyNotion.domain.ProfileDto;
import in.studyNotion.models.Profile;
import in.studyNotion.models.User;
import in.studyNotion.repositories.ProfileRepository;
import in.studyNotion.repositories.UserRepository;
import in.studyNotion.services.ProfileService;
import in.studyNotion.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
public class ProfileServiceImple implements ProfileService {

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtils jwtUtils;


    @Transactional
    @Override
    public boolean updateProfile(ProfileDto profileDto,String jwt) {

        boolean updated = false;
        try{

            String userEmail  = jwtUtils.getEmailFromToken(jwt);
            User user = this.userRepository.findByEmail(userEmail);

            ObjectId profileId = user.getProfile().getId();

            Optional<Profile> existsProfile = this.profileRepository.findById(profileId);
            if(existsProfile.isPresent()){

                Profile profile = existsProfile.get();

                profile.setAbout(profileDto.getAbout()!=null && !profileDto.getAbout().equals("")? profileDto.getAbout() : profile.getAbout());
                profile.setGender(profileDto.getGender()!=null && !profileDto.getGender().equals("")?profileDto.getGender():profile.getGender());
                profile.setDateOfBirth(profileDto.getDateOfBirth()!=null && !profileDto.getDateOfBirth().equals("")?profileDto.getDateOfBirth():profile.getDateOfBirth());

                this.profileRepository.save(profile);
                updated=true;

                return updated;
            }
            else{
                return updated;
            }
        }catch (Exception e){
            log.error("Error occurred while update user profile "+e.getMessage());
            return updated;
        }
    }

    @Override
    public Profile getUserProfile(String jwt) {

        String userEmail  = jwtUtils.getEmailFromToken(jwt);
        User user = this.userRepository.findByEmail(userEmail);
        return user.getProfile();
    }


}

package in.studyNotion.services;

import in.studyNotion.domain.ProfileDto;
import in.studyNotion.models.Profile;
import org.bson.types.ObjectId;

import java.util.Optional;

public interface ProfileService {

    boolean updateProfile(ProfileDto profileDto,String jwt);
    Profile getUserProfile(String jwt);

}

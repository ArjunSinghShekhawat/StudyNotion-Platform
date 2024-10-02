package in.study_notion.services;

import in.study_notion.domain.ProfileDto;
import in.study_notion.models.Profile;

public interface ProfileService {

    boolean updateProfile(ProfileDto profileDto,String jwt);
    Profile getUserProfile(String jwt);

}

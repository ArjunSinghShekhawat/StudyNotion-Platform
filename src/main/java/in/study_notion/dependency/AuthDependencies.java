package in.study_notion.dependency;

import in.study_notion.repositories.ProfileRepository;
import in.study_notion.repositories.UserRepository;
import in.study_notion.utils.JwtUtils;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Getter
public class AuthDependencies {
    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;
    private final JwtUtils jwtUtils;

    @Autowired
    public AuthDependencies(UserRepository userRepository,
                            ProfileRepository profileRepository,
                            JwtUtils jwtUtils) {
        this.userRepository = userRepository;
        this.profileRepository = profileRepository;
        this.jwtUtils = jwtUtils;
    }
}


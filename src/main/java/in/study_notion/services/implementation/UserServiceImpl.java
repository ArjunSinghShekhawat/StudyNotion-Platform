package in.study_notion.services.implementation;

import in.study_notion.models.User;
import in.study_notion.repositories.UserRepository;
import in.study_notion.request.UserRequest;
import in.study_notion.services.UserService;
import in.study_notion.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserServiceImpl implements UserService {


    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, JwtUtils jwtUtils){
        this.userRepository = userRepository;
        this.jwtUtils=jwtUtils;
    }


    @Override
    public User updateUser(String jwt, UserRequest userRequest) {
        /*
        This method is used for update user
         */

           String userEmail  = jwtUtils.getEmailFromToken(jwt);
           User user = this.userRepository.findByEmail(userEmail);

           user.setFirstName(userRequest.getFirstName()!=null && !userRequest.getFirstName().isEmpty() ?userRequest.getFirstName():user.getFirstName());
           user.setLastName(userRequest.getLastName()!=null && !userRequest.getLastName().isEmpty() ?userRequest.getLastName():user.getLastName());

           return this.userRepository.save(user);
    }

    @Override
    public User getUserAllInformationByEmail(String jwt){
        /*
        This method is used for get all information about the user
         */

        String email = this.jwtUtils.getEmailFromToken(jwt);
        return this.userRepository.findByEmail(email);
    }
}

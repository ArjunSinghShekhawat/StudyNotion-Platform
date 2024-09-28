package in.studyNotion.services.implement;

import in.studyNotion.models.User;
import in.studyNotion.repositories.UserRepository;
import in.studyNotion.request.UserRequest;
import in.studyNotion.services.UserService;
import in.studyNotion.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImple implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public User updateUser(String jwt, UserRequest userRequest) {

        String userEmail  = jwtUtils.getEmailFromToken(jwt);
        User user = this.userRepository.findByEmail(userEmail);

        user.setFirstName(userRequest.getFirstName());
        user.setLastName(userRequest.getLastName());

       return this.userRepository.save(user);
    }
}

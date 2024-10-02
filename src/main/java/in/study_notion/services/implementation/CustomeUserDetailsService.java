package in.study_notion.services.implementation;

import in.study_notion.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CustomeUserDetailsService implements UserDetailsService {


    private final UserRepository userRepository;

    @Autowired
    public CustomeUserDetailsService(UserRepository userRepository){
        this.userRepository=userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        in.study_notion.models.User user = this.userRepository.findByEmail(username);

        if(user!=null){
            return User.builder()
                    .username(user.getEmail())
                    .password(user.getPassword())
                    .roles(String.valueOf(user.getAccountTypes()))
                    .build();
        }
        log.error("Error Occurred while load the user from the database but not found");
        throw new UsernameNotFoundException("User Not Found");
    }
}

package in.studyNotion.services.implement;

import in.studyNotion.repositories.UserRepository;
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

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        in.studyNotion.models.User user = this.userRepository.findByEmail(username);

        if(user!=null){
            return User.builder()
                    .username(user.getEmail())
                    .password(user.getPassword())
                    .roles(String.valueOf(user.getAccountTypes()))
                    .build();
        }
        System.out.println("load user "+user);

        log.error("Error Occurred while load the user from the database but not found");
        throw new UsernameNotFoundException("User Not Found");
    }
}

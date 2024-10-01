package in.studyNotion.services;

import in.studyNotion.models.User;
import in.studyNotion.repositories.CourseRepository;
import in.studyNotion.repositories.UserRepository;
import in.studyNotion.utils.JwtUtils;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class getInstructirCourse {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtils jwtUtils;

    @Disabled
    @ParameterizedTest
    @CsvSource("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhcmp1bnNoZWtoYXdhdDI4MDRAZ21haWwuY29tIiwiaWF0IjoxNzI3NzU2MzA3LCJleHAiOjE3Mjc4NDI3MDd9.a6z-tnuR6xS5kpXZCxvG0ANQ5Iu5crKB1vlQIs_S26E \"66fb6a42440aa76c97772a87\"")
    public void check1(String jwt,ObjectId id){

        assertNotNull(this.jwtUtils.getEmailFromToken(jwt));
        String email = this.jwtUtils.getEmailFromToken(jwt);

        assertNotNull(this.userRepository.findByEmail(email));
        User user = this.userRepository.findByEmail(email);


        assertNotNull(user.getCourses().contains(id));

    }
}

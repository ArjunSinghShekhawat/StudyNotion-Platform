package in.studyNotion.services.implement;

import in.studyNotion.constants.Constant;
import in.studyNotion.domain.CourseDto;
import in.studyNotion.enums.Status;
import in.studyNotion.exceptions.ResponceNotFoundException;
import in.studyNotion.models.Category;
import in.studyNotion.models.Course;
import in.studyNotion.models.User;
import in.studyNotion.repositories.*;
import in.studyNotion.request.CourseRequest;
import in.studyNotion.services.CourseService;
import in.studyNotion.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

@Slf4j
@Service
public class CourseServiceImple implements CourseService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private EmailSenderService emailSenderService;


    @Override
    public Course createCourse(CourseRequest courseRequest, String jwt, ObjectId categoryId,String secureUrl) throws Exception {

        try{
            //fetch the user
            String email = this.jwtUtils.getEmailFromToken(jwt);
            User existsUser = this.userRepository.findByEmail(email);

            //check status
            if(courseRequest.getStatus()==null){
                courseRequest.setStatus(Status.DRAFT);
            }
            //user instructor or not check already
            Optional<Category> category = this.categoryRepository.findById(categoryId);

            if(!category.isPresent()){
                throw new ResponceNotFoundException("In Course Controller Category Details Not Found ","category","category Id");
            }

            //create course
            Course course = new Course();

            course.setCourseName(courseRequest.getCourseName());
            course.setCourseDescription(courseRequest.getCourseDescription());
            course.getInstructions().addAll(courseRequest.getInstructions());
            course.setWhatYouWillLearn(courseRequest.getWhatYouWillLearn());
            course.setPrice(courseRequest.getPrice());
            course.getTags().addAll(courseRequest.getTags());
            course.setCategory(category.get());
            course.setStatus(courseRequest.getStatus());
            course.setInstructor(existsUser);
            course.setCreatedAt(LocalDateTime.now());
            course.setThumbnail(secureUrl);


            //course saved
            Course newCourse = this.courseRepository.save(course);

            //add into category
            category.get().getCourses().add(newCourse);
            this.categoryRepository.save(category.get());

            //update user
            existsUser.getCourses().add(newCourse);
            this.userRepository.save(existsUser);

            this.emailSenderService.sendMail(existsUser.getEmail(),"Course Created Successfully",String.format("%s  Course Created By %s : ",newCourse.getCourseName(),existsUser.getFirstName()));

            return newCourse;
        }catch (Exception e){
            log.error("Error Occurred while create new Course {} ",e.getMessage());
            throw new Exception("Course not created");
        }
    }
}

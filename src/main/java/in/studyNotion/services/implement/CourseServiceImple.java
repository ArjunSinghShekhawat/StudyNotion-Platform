package in.studyNotion.services.implement;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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
    private CloudinaryDocumentUploadServiceImple cloudinaryDocumentUploadServiceImple;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private SectionRepository sectionRepository;

    @Autowired
    private SubSectionRepository subSectionRepository;

    @Autowired
    private CourseRepository courseRepository;


    @Override
    public Course createCourse(CourseRequest courseRequest, String jwt, MultipartFile file) throws Exception {

        try{
            //fetch the user
            String email = this.jwtUtils.getEmailFromToken(jwt);
            User existsUser = this.userRepository.findByEmail(email);

            //check status
            if(courseRequest.getStatus()==null){
                courseRequest.setStatus(Status.DRAFT);
            }
            //user instructor or not check already

            Optional<Category> category = this.categoryRepository.findById(courseRequest.getCategory());

            if(!category.isPresent()){
                throw new ResponceNotFoundException("In Course Controller Category Details Not Found ","category","category Id");
            }

            //upload thumbnail
            Map  thumbnailData = this.cloudinaryDocumentUploadServiceImple.documentUploader(file,"StudyNotion/thumbnail");

            //create course
            Course course = new Course();

            course.setCourseName(courseRequest.getCourseName());
            course.setCourseDescription(course.getCourseDescription());
            course.setInstructions(courseRequest.getInstructions());
            course.setWhatYouWillLearn(course.getWhatYouWillLearn());
            course.setPrice(courseRequest.getPrice());
            course.setTags(courseRequest.getTags());
            course.setCategory(category.get().getId());
            course.setThumbnail((String)thumbnailData.get("secure_url"));
            course.setStatus(courseRequest.getStatus());
            course.setInstructor(existsUser.getId());

            //course saved
            Course newCourse = this.courseRepository.save(course);

            //update user
            existsUser.getCourses().add(newCourse);
            this.userRepository.save(existsUser);

            return newCourse;
        }catch (Exception e){
            log.error("Error Occurred while create new Course {} ",e.getMessage());
            throw new Exception("Course not created");
        }
    }
}

package in.studyNotion.controllers;

import in.studyNotion.constants.Constant;
import in.studyNotion.domain.CourseDto;
import in.studyNotion.models.Course;
import in.studyNotion.models.User;
import in.studyNotion.repositories.CourseRepository;
import in.studyNotion.repositories.UserRepository;
import in.studyNotion.request.CourseRequest;
import in.studyNotion.services.CourseService;
import in.studyNotion.services.implement.CloudinaryDocumentUploadServiceImple;
import in.studyNotion.services.implement.EmailSenderService;
import in.studyNotion.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/instructor/course")
public class CourseControllers {


    @Autowired
    private CourseService courseService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private CloudinaryDocumentUploadServiceImple cloudinaryDocumentUploadServiceImple;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private EmailSenderService emailSenderService;


    @Autowired
    private UserRepository userRepository;

    @PostMapping("/create/{categoryId}")
    public ResponseEntity<Course>uploadThumbnail(
                                               @PathVariable ObjectId categoryId,
                                               @RequestHeader("Authorization") String jwt,
                                               @RequestPart("courseRequest") CourseRequest courseRequest){

        try{
            String secureUrl="arjun.png";
            Course course = this.courseService.createCourse(courseRequest, jwt,categoryId,secureUrl);
            return new ResponseEntity<>(course, HttpStatus.CREATED);

        }catch (Exception e){
            log.error("Error occurred while creating a new Course {} ",e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/thumbnail-upload/{courseId}")
    public ResponseEntity<Course>videoUpload(@RequestParam("file")MultipartFile file,@PathVariable ObjectId courseId){

        try{
            Optional<Course> course = this.courseRepository.findById(courseId);

            if(course.isPresent()){
                String folder=Constant.COURSE_FOLDER;
                String fileName=course.get().getCourseName();

                Map upload = this.cloudinaryDocumentUploadServiceImple.upload(file,folder,fileName);

                String secureUrl = (String)upload.get("secure_url");
                course.get().setThumbnail(secureUrl);

                Course updatedCourse = this.courseRepository.save(course.get());

                String message = String.format("Course Created Successfully \n Course Name -> %s \n Price -> %d \n ",updatedCourse.getCourseName(),updatedCourse.getPrice());

                this.emailSenderService.sendMail(updatedCourse.getInstructor().getEmail(),"Course Creation",message);

                return new ResponseEntity<>(updatedCourse, HttpStatus.OK);
            }

        }catch (Exception e) {
            log.error("Error occurred while upload thumbnail {} ", e.getMessage());
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}

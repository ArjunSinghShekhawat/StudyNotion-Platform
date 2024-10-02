package in.study_notion.controllers;

import in.study_notion.constants.Constant;
import in.study_notion.models.Course;
import in.study_notion.repositories.CourseRepository;
import in.study_notion.repositories.UserRepository;
import in.study_notion.request.CourseRequest;
import in.study_notion.services.CourseService;
import in.study_notion.services.implementation.CloudinaryDocumentUploadServiceImpl;
import in.study_notion.services.implementation.EmailSenderService;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Slf4j
@RestController
@RequestMapping("/instructor/course")
public class CourseControllers {


    @Autowired
    private CourseService courseService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private  CloudinaryDocumentUploadServiceImpl cloudinaryDocumentUploadService;


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
                                               @RequestBody CourseRequest courseRequest){

        try{

            Course course = this.courseService.createCourse(courseRequest, jwt,categoryId);
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

                Map upload = this.cloudinaryDocumentUploadService.upload(file,folder,fileName);

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

    @PutMapping("/update/{courseId}")
    public ResponseEntity<Boolean>updateCourse(@RequestBody CourseRequest courseRequest,@PathVariable ObjectId courseId,@RequestHeader("Authorization") String jwt){
        try{
            boolean isUpdate = this.courseService.courseUpdate(courseRequest, jwt, courseId);

            if(isUpdate){
                return new ResponseEntity<>(isUpdate,HttpStatus.OK);
            }
            else{
                return new ResponseEntity<>(isUpdate,HttpStatus.BAD_GATEWAY);
            }

        }catch (Exception e){
            log.error("Error occur while update course {} ",e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    @DeleteMapping("/delete/{courseId}")
    public ResponseEntity<Boolean>deleteCourse(@PathVariable ObjectId courseId,@RequestHeader("Authorization") String jwt){
        try{
            boolean isRemove = this.courseService.deleteCourse(courseId,jwt);

            if(isRemove){
                return new ResponseEntity<>(isRemove,HttpStatus.OK);
            }
            else{
                return new ResponseEntity<>(isRemove,HttpStatus.BAD_GATEWAY);
            }

        }catch (Exception e){
            log.error("Error occur while remove course {} ",e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/get-instructor-courses")
    public ResponseEntity<Set<Course>>getInstructorCourses(@RequestHeader("Authorization") String jwt){
        try{
            Set<Course> allInstructorCourse = this.courseService.getAllInstructorCourse(jwt);

            if(allInstructorCourse!=null){
                return new ResponseEntity<>(allInstructorCourse,HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        }catch (Exception e){
            log.error("Error occur while get all instructor courses {} ",e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}

package in.studyNotion.controllers;

import in.studyNotion.constants.Constant;
import in.studyNotion.domain.CourseDto;
import in.studyNotion.models.Course;
import in.studyNotion.models.User;
import in.studyNotion.repositories.UserRepository;
import in.studyNotion.request.CourseRequest;
import in.studyNotion.services.CourseService;
import in.studyNotion.services.implement.CloudinaryDocumentUploadServiceImple;
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

@Slf4j
@RestController
@RequestMapping("/instructor/course")
public class CourseControllers {


    @Autowired
    private CourseService courseService;

    @Autowired
    private ModelMapper modelMapper;


    @Autowired
    private UserRepository userRepository;

    @PostMapping("/thumbnail/{categoryId}")
    public ResponseEntity<Course>uploadThumbnail(
                                               @PathVariable ObjectId categoryId,
                                               @RequestHeader("Authorization") String jwt,
                                               @RequestBody CourseRequest courseRequest){

        try{

           String secureUrl = "arjun.png";

            Course course = this.courseService.createCourse(courseRequest, jwt,categoryId,secureUrl);
            return new ResponseEntity<>(course, HttpStatus.CREATED);

        }catch (Exception e){
            log.error("Error occurred while creating a new Course {} ",e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}

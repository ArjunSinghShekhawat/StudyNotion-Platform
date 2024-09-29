package in.studyNotion.controllers;

import in.studyNotion.domain.CourseDto;
import in.studyNotion.models.Course;
import in.studyNotion.request.CourseRequest;
import in.studyNotion.services.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/course")
public class CourseControllers {


    @Autowired
    private CourseService courseService;
    @PostMapping("/create")
    public ResponseEntity<CourseDto>createNewCourse(@RequestParam("file") MultipartFile file,
                                                 @RequestHeader("Authorization") String jwt,
                                                 @RequestBody CourseRequest courseRequest){

        try{
            Course course = this.courseService.createCourse(courseRequest, jwt, file);

        }catch (Exception e){

        }

        return null;
    }
}

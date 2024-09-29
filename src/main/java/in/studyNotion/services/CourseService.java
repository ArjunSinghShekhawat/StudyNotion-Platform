package in.studyNotion.services;

import in.studyNotion.domain.CourseDto;
import in.studyNotion.models.Course;
import in.studyNotion.request.CourseRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.concurrent.ExecutionException;

public interface CourseService {
    Course createCourse(CourseRequest courseRequest, String jwt, MultipartFile file) throws Exception;
}

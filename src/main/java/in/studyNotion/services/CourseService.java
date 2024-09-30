package in.studyNotion.services;

import in.studyNotion.domain.CourseDto;
import in.studyNotion.models.Course;
import in.studyNotion.request.CourseRequest;
import org.bson.types.ObjectId;
import org.springframework.web.multipart.MultipartFile;

import java.util.concurrent.ExecutionException;

public interface CourseService {
    Course createCourse(CourseRequest courseRequest, String jwt, ObjectId categoryId,String secureUrl) throws Exception;
}

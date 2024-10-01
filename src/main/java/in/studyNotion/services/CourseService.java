package in.studyNotion.services;

import in.studyNotion.domain.CourseDto;
import in.studyNotion.models.Course;
import in.studyNotion.request.CourseRequest;
import org.bson.types.ObjectId;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;
import java.util.concurrent.ExecutionException;

public interface CourseService {
    Course createCourse(CourseRequest courseRequest, String jwt, ObjectId categoryId) throws Exception;
    boolean courseUpdate(CourseRequest courseRequest,String jwt,ObjectId courseId);
    boolean deleteCourse(ObjectId courseId,String jwt);
    public Set<Course> getAllInstructorCourse(String jwt);
}

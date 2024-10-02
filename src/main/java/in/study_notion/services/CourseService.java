package in.study_notion.services;

import in.study_notion.models.Course;
import in.study_notion.request.CourseRequest;
import org.bson.types.ObjectId;

import java.util.Set;

public interface CourseService {
    Course createCourse(CourseRequest courseRequest, String jwt, ObjectId categoryId) throws Exception;
    boolean courseUpdate(CourseRequest courseRequest,String jwt,ObjectId courseId);
    boolean deleteCourse(ObjectId courseId,String jwt);
    public Set<Course> getAllInstructorCourse(String jwt);
}

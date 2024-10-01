package in.studyNotion.services.implement;

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
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

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

    @Transactional
    @Override
    public Course createCourse(CourseRequest courseRequest, String jwt, ObjectId categoryId) throws Exception {
        try {
            User user = getUserByJwt(jwt);
            Category category = getCategoryById(categoryId);

            // Set status if not provided
            if (courseRequest.getStatus() == null) {
                courseRequest.setStatus(Status.DRAFT);
            }

            Course course = buildCourse(courseRequest, category, user);
            Course savedCourse = courseRepository.save(course);

            category.getCourses().add(savedCourse);
            categoryRepository.save(category);

            user.getCourses().add(savedCourse);
            userRepository.save(user);

            boolean emailSent = emailSenderService.sendMail(user.getEmail(), "Course Created Successfully",
                    String.format("%s Course Created By %s", savedCourse.getCourseName(), user.getFirstName()));

            log.info("New course created with ID: {}. Email sent: {}", savedCourse.getId(), emailSent);

            return savedCourse;
        } catch (Exception e) {
            log.error("Error occurred while creating course: {}", e.getMessage());
            throw new Exception("Course creation failed", e);
        }
    }

    @Transactional
    @Override
    public boolean courseUpdate(CourseRequest courseRequest, String jwt, ObjectId courseId) {
        try {
            User user = getUserByJwt(jwt);
            Course course = getInstructorCourse(courseId, user);

            updateCourseDetails(course, courseRequest);
            courseRepository.save(course);

            log.info("Course updated successfully with ID: {}", course.getId());
            return true;
        } catch (Exception e) {
            log.error("Error occurred while updating course: {}", e.getMessage());
            return false;
        }
    }

    @Transactional
    @Override
    public boolean deleteCourse(ObjectId courseId, String jwt) {
        try {
            User user = getUserByJwt(jwt);
            Course course = getInstructorCourse(courseId, user);

            Category category = course.getCategory();
            removeCourseFromCategory(category, courseId);

            removeCourseFromUser(user, courseId);

            courseRepository.deleteById(courseId);
            log.info("Course deleted with ID: {}", courseId);
            return true;
        } catch (Exception e) {
            log.error("Error occurred while deleting course: {}", e.getMessage());
            return false;
        }
    }
    @Override
    public Set<Course> getAllInstructorCourse(String jwt){
        User user = getUserByJwt(jwt);
        return user.getCourses();
    }






    private User getUserByJwt(String jwt) throws ResponceNotFoundException {
        String email = jwtUtils.getEmailFromToken(jwt);
        return Optional.ofNullable(userRepository.findByEmail(email))
                .orElseThrow(() -> new ResponceNotFoundException("User not found", "email", email));
    }

    private Category getCategoryById(ObjectId categoryId) throws ResponceNotFoundException {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResponceNotFoundException("Category not found", "categoryId", categoryId.toHexString()));
    }

    private Course getInstructorCourse(ObjectId courseId, User user) throws Exception {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new Exception("Course not found"));

        if (!user.getCourses().contains(course)) {
            throw new Exception("Course not assigned to this instructor");
        }

        return course;
    }

    private void updateCourseDetails(Course course, CourseRequest courseRequest) {
        if (courseRequest.getCourseName() != null && !courseRequest.getCourseName().isEmpty()) {
            course.setCourseName(courseRequest.getCourseName());
        }
        if (courseRequest.getCourseDescription() != null && !courseRequest.getCourseDescription().isEmpty()) {
            course.setCourseDescription(courseRequest.getCourseDescription());
        }
        if (courseRequest.getTags() != null && !courseRequest.getTags().isEmpty()) {
            course.setTags(courseRequest.getTags());
        }
        if (courseRequest.getInstructions() != null && !courseRequest.getInstructions().isEmpty()) {
            course.setInstructions(courseRequest.getInstructions());
        }
        if (courseRequest.getWhatYouWillLearn() != null && !courseRequest.getWhatYouWillLearn().isEmpty()) {
            course.setWhatYouWillLearn(courseRequest.getWhatYouWillLearn());
        }
        if (courseRequest.getPrice() != null) {
            course.setPrice(courseRequest.getPrice());
        }
        if (courseRequest.getStatus() != null) {
            course.setStatus(courseRequest.getStatus());
        }
    }

    private void removeCourseFromCategory(Category category, ObjectId courseId) throws Exception {
        boolean removed = category.getCourses().removeIf(c -> c.getId().equals(courseId));
        if (!removed) {
            throw new Exception("Failed to remove course from category");
        }
        categoryRepository.save(category);
    }

    private void removeCourseFromUser(User user, ObjectId courseId) throws Exception {
        boolean removed = user.getCourses().removeIf(c -> c.getId().equals(courseId));
        if (!removed) {
            throw new Exception("Failed to remove course from user's courses");
        }
        userRepository.save(user);
    }

    private Course buildCourse(CourseRequest request, Category category, User user) {
        Course course = new Course();
        course.setCourseName(request.getCourseName());
        course.setCourseDescription(request.getCourseDescription());
        course.setInstructions(request.getInstructions());
        course.setWhatYouWillLearn(request.getWhatYouWillLearn());
        course.setPrice(request.getPrice());
        course.setTags(request.getTags());
        course.setCategory(category);
        course.setStatus(request.getStatus());
        course.setInstructor(user);
        course.setCreatedAt(LocalDateTime.now());
        return course;
    }
}

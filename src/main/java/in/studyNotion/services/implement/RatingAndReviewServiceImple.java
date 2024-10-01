package in.studyNotion.services.implement;

import in.studyNotion.models.Course;
import in.studyNotion.models.RatingAndReview;
import in.studyNotion.models.User;
import in.studyNotion.repositories.CourseRepository;
import in.studyNotion.repositories.RatingAndReviewRepository;
import in.studyNotion.repositories.UserRepository;
import in.studyNotion.request.RatingAndReviewRequest;
import in.studyNotion.services.RatingAndReviewService;
import in.studyNotion.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class RatingAndReviewServiceImple implements RatingAndReviewService {

    @Autowired
    private RatingAndReviewRepository ratingAndReviewRepository;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseRepository courseRepository;


    @Transactional
    @Override
    public RatingAndReview createRatingAndReview(RatingAndReviewRequest ratingAndReviewRequest, String jwt) throws Exception {

        try{
            //fetch user info
            String email = this.jwtUtils.getEmailFromToken(jwt);
            User user = this.userRepository.findByEmail(email);

            Optional<Course> courseOptional = this.courseRepository.findById(ratingAndReviewRequest.getCourseId());

            if(courseOptional.isPresent()){
                Course course = courseOptional.get();

                //check user enrolled or not
                boolean isEnrolled = false;

               for(User u:course.getStudentsEnrolled()){
                   if(u.getId().equals(user.getId())){
                       isEnrolled = true;
                       break;
                   }
               }

               if(!isEnrolled){
                   throw new Exception("Student not enrolled !");
               }

              //create rating and review
                RatingAndReview ratingAndReview = new RatingAndReview();

               ratingAndReview.setReview(ratingAndReviewRequest.getReview());
               ratingAndReview.setRating(ratingAndReviewRequest.getRating());
               ratingAndReview.setUser(user);
               ratingAndReview.setCourse(course);

               //save rating and review
               RatingAndReview savedRatingAndReview = this.ratingAndReviewRepository.save(ratingAndReview);


                //save updated course details
                course.getRatingAndReviews().add(savedRatingAndReview);
                this.courseRepository.save(course);

                return ratingAndReview;

            }

        }catch (Exception e){
            log.error("Error occurred while create rating and review {} ",e.getMessage());
        }
        throw new Exception("rating and review not created Exception");
    }

    @Override
    public List<RatingAndReview> getAllRatingAndReview() {
        return this.ratingAndReviewRepository.findAll();
    }

}

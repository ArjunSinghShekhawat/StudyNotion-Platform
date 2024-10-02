package in.study_notion.services.implementation;

import in.study_notion.models.Course;
import in.study_notion.models.RatingAndReview;
import in.study_notion.models.User;
import in.study_notion.repositories.CourseRepository;
import in.study_notion.repositories.RatingAndReviewRepository;
import in.study_notion.repositories.UserRepository;
import in.study_notion.request.RatingAndReviewRequest;
import in.study_notion.services.RatingAndReviewService;
import in.study_notion.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class RatingAndReviewServiceImpl implements RatingAndReviewService {

    private final RatingAndReviewRepository ratingAndReviewRepository;
    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;


    @Autowired
    public RatingAndReviewServiceImpl(RatingAndReviewRepository ratingAndReviewRepository,
                            JwtUtils jwtUtils,
                            UserRepository userRepository,
                            CourseRepository courseRepository) {
        this.ratingAndReviewRepository = ratingAndReviewRepository;
        this.jwtUtils = jwtUtils;
        this.userRepository = userRepository;
        this.courseRepository = courseRepository;
    }

    @Transactional
    @Override
    public RatingAndReview createRatingAndReview(RatingAndReviewRequest ratingAndReviewRequest, String jwt){

        /*
        This Method is used for creating a rating and review
         */

        RatingAndReview ratingAndReview = null;
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
                   throw new NoSuchFieldException("Student not enrolled !");
               }

               //creating a rating and review
               ratingAndReview = new RatingAndReview();

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
        return ratingAndReview;
    }

    @Override
    public List<RatingAndReview> getAllRatingAndReview() {
        /*
        This method is used for get all rating and review
         */
        return this.ratingAndReviewRepository.findAll();
    }

}

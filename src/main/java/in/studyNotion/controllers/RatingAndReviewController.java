package in.studyNotion.controllers;

import in.studyNotion.models.RatingAndReview;
import in.studyNotion.request.RatingAndReviewRequest;
import in.studyNotion.services.RatingAndReviewService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/student/rating-review")
@Slf4j
public class RatingAndReviewController {

    @Autowired
    private RatingAndReviewService ratingAndReviewService;

    @PostMapping("/create")
    public ResponseEntity<RatingAndReview>createRatingAndReview(@RequestBody RatingAndReviewRequest ratingAndReviewRequest,@RequestHeader("Authorization") String jwt){
        try{
            RatingAndReview ratingAndReview = this.ratingAndReviewService.createRatingAndReview(ratingAndReviewRequest, jwt);

            if(ratingAndReview!=null){
                return new ResponseEntity<>(ratingAndReview,HttpStatus.CREATED);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }catch (Exception e){
            log.error("Error occured while create rating and review {} ",e.getMessage());
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}

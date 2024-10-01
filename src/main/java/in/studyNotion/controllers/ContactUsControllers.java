package in.studyNotion.controllers;

import in.studyNotion.domain.CategoryDto;
import in.studyNotion.models.Category;
import in.studyNotion.models.ContactUs;
import in.studyNotion.services.implement.ContactUsService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/contact-us")
@Slf4j
public class ContactUsControllers {

    @Autowired
    private ContactUsService contactUsService;

    @PostMapping("/query")
    public ResponseEntity<Boolean> contactUs(@RequestBody ContactUs contactUs){
        try{
            boolean query = this.contactUsService.contactUs(contactUs);

            if(query){
                return new ResponseEntity<>(query,HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }catch (Exception e){
            log.error("Error occurred while create new contact us query {} ",e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
    }


}

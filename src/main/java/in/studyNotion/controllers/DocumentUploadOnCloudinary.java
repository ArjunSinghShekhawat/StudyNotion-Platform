package in.studyNotion.controllers;

import in.studyNotion.constants.Constant;
import in.studyNotion.enums.AccountTypes;
import in.studyNotion.models.Course;
import in.studyNotion.models.User;
import in.studyNotion.repositories.CourseRepository;
import in.studyNotion.repositories.UserRepository;
import in.studyNotion.services.implement.CloudinaryDocumentUploadServiceImple;
import in.studyNotion.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/upload")
public class DocumentUploadOnCloudinary {

    @Autowired
    private CloudinaryDocumentUploadServiceImple cloudinaryDocumentUploadServiceImple;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseRepository courseRepository;


    @PutMapping("/photo")
    public ResponseEntity<User>uploadDocument(@RequestParam("file")MultipartFile file,@RequestHeader("Authorization") String jwt){

       try{

           //user fetch
           String email = this.jwtUtils.getEmailFromToken(jwt);
           User user = this.userRepository.findByEmail(email);

           AccountTypes role = user.getAccountTypes();

           String folder=null;

           //folder selection
           if(role.equals(AccountTypes.STUDENT)){
               folder = Constant.STUDENT_FOLDER;
           } else if (role.equals(AccountTypes.ADMIN)) {
               folder = Constant.ADMIN_FOLDER;
           } else {
               folder = Constant.INSTRUCTOR_FOLDER;

           }

           String fileName=user.getFirstName();

           Map upload = this.cloudinaryDocumentUploadServiceImple.upload(file,folder,fileName);

           //url saved
           String secureUrl = (String)upload.get("secure_url");
           user.setImageUrl(secureUrl);


           //user saved
           User updatedUser = this.userRepository.save(user);

           return new ResponseEntity<>(updatedUser, HttpStatus.OK);

       }catch (Exception e) {
           log.error("Error occurred while upload file {} ", e.getMessage());
           return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
       }
    }




}

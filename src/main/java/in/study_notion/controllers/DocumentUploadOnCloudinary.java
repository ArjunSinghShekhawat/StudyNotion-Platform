package in.study_notion.controllers;

import in.study_notion.constants.Constant;
import in.study_notion.enums.AccountTypes;
import in.study_notion.exceptions.CloudinaryUploadException;
import in.study_notion.exceptions.GeneralExceptionHandler;
import in.study_notion.models.User;
import in.study_notion.repositories.UserRepository;
import in.study_notion.services.implementation.CloudinaryDocumentUploadServiceImpl;
import in.study_notion.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Slf4j
@RestController
@RequestMapping("/upload")
public class DocumentUploadOnCloudinary {

    private final CloudinaryDocumentUploadServiceImpl cloudinaryDocumentUploadServiceImpl;
    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;

    @Autowired
    public DocumentUploadOnCloudinary(CloudinaryDocumentUploadServiceImpl cloudinaryDocumentUploadServiceImpl,
                          JwtUtils jwtUtils,
                          UserRepository userRepository
    ) {
        this.cloudinaryDocumentUploadServiceImpl = cloudinaryDocumentUploadServiceImpl;
        this.jwtUtils = jwtUtils;
        this.userRepository = userRepository;
    }

    @PutMapping("/photo")
    public ResponseEntity<User>uploadDocument(@RequestParam("file")MultipartFile file,@RequestHeader("Authorization") String jwt){

       try{
           /*
             This Method is used to upload document int cloudinary
           */

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

           //set filename
           String fileName=user.getFirstName();

           Map<?, ?> upload = this.cloudinaryDocumentUploadServiceImpl.upload(file,folder,fileName);

           //url saved
           String secureUrl = (String)upload.get("secure_url");
           user.setImageUrl(secureUrl);

           //user saved
           User updatedUser = this.userRepository.save(user);

           return new ResponseEntity<>(updatedUser, HttpStatus.OK);

       }catch (GeneralExceptionHandler e) {
           log.error("Error occurred while upload file {} ", e.getMessage());
           Thread.currentThread().interrupt();
           return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
       } catch (CloudinaryUploadException e) {
           throw new RuntimeException(e);
       } catch (IOException e) {
           throw new RuntimeException(e);
       }
    }
}

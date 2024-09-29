package in.studyNotion.controllers;

import in.studyNotion.services.implement.CloudinaryDocumentUploadServiceImple;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/cloudinary-upload")
public class DocumentUploadOnCloudinary {

    @Autowired
    private CloudinaryDocumentUploadServiceImple cloudinaryDocumentUploadServiceImple;


    @PostMapping("/data")
    public ResponseEntity<Map>uploadDocument(@RequestParam("file")MultipartFile file,@RequestHeader("Authorization") String jwt){

       try{
           Map upload = this.cloudinaryDocumentUploadServiceImple.upload(file,jwt);
           return new ResponseEntity<>(upload, HttpStatus.OK);
       }catch (Exception e){
           log.error("Error occurred while upload file {} ",e.getMessage());
           return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
       }
    }
}

package in.studyNotion.controllers;

import in.studyNotion.constants.Constant;
import in.studyNotion.models.SubSection;
import in.studyNotion.repositories.SubSectionRepository;
import in.studyNotion.request.SubSectionRequest;
import in.studyNotion.services.SubSectionService;
import in.studyNotion.services.implement.CloudinaryDocumentUploadServiceImple;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.Map;

@RestController
@RequestMapping("instructor/sub-section")
@Slf4j
public class SubSectionController {

    @Autowired
    private SubSectionService subSectionService;

    @Autowired
    private SubSectionRepository subSectionRepository;

    @Autowired
    private CloudinaryDocumentUploadServiceImple cloudinaryDocumentUploadServiceImple;


    @PutMapping("/video-upload/{subSectionId}")
    @Transactional
    public ResponseEntity<SubSection>videoUpload(@RequestParam("file") MultipartFile file, @PathVariable ObjectId subSectionId){

        try{
            SubSection subSection = this.subSectionService.getSubSectionById(subSectionId);

            if(subSection!=null){
                String folder= Constant.VIDEO_FOLDER;
                String fileName=subSection.getSubSectionName();

                Map upload = this.cloudinaryDocumentUploadServiceImple.upload(file,folder,fileName);

                String secureUrl = (String)upload.get("secure_url");
                Object duration = upload.get("duration");


                subSection.setVideoUrl(secureUrl);
                subSection.setTimeDuration(String.valueOf(duration));

                SubSection updtedSubSection = this.subSectionRepository.save(subSection);

                return new ResponseEntity<>(updtedSubSection, HttpStatus.OK);
            }

        }catch (Exception e) {
            log.error("Error occurred while upload thumbnail {} ", e.getMessage());
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/create")
    public ResponseEntity<SubSection>createSubSection(@RequestBody SubSectionRequest subSectionRequest){

        try{
            SubSection subSection = this.subSectionService.createSubSection(subSectionRequest);

            if(subSection!=null){
                return new ResponseEntity<>(subSection, HttpStatus.CREATED);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


    @PutMapping("/update/{subSectionId}")
    public ResponseEntity<Boolean>updateSubSection(@RequestBody SubSectionRequest subSectionRequest, @PathVariable ObjectId subSectionId){

        try{
            boolean isUpdate = this.subSectionService.updateSubSection(subSectionRequest, subSectionId);

            if(isUpdate){
                return new ResponseEntity<>(isUpdate,HttpStatus.OK);
            }
            return new ResponseEntity<>(isUpdate,HttpStatus.NOT_FOUND);

        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/delete/{subSectionId}/{sectionId}")
    public ResponseEntity<Boolean>deleteSubSection(@PathVariable ObjectId subSectionId,@PathVariable ObjectId sectionId){

        try{
            boolean isDelete = this.subSectionService.deleteSubSection(subSectionId,sectionId);

            if(isDelete){
                return new ResponseEntity<>(isDelete,HttpStatus.OK);
            }
            return new ResponseEntity<>(isDelete,HttpStatus.NOT_FOUND);

        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/get/{subSectionId}")
    public ResponseEntity<SubSection>deleteSubSection(@PathVariable ObjectId subSectionId){

        try{
            SubSection subSection = this.subSectionService.getSubSectionById(subSectionId);

            if(subSection!=null){
                return new ResponseEntity<>(subSection,HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

}

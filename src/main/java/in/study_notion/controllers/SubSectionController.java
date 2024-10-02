package in.study_notion.controllers;

import in.study_notion.constants.Constant;
import in.study_notion.models.SubSection;
import in.study_notion.repositories.SubSectionRepository;
import in.study_notion.request.SubSectionRequest;
import in.study_notion.services.SubSectionService;
import in.study_notion.services.implementation.CloudinaryDocumentUploadServiceImpl;
import jakarta.validation.Valid;
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

    private final SubSectionService subSectionService;
    private final SubSectionRepository subSectionRepository;
    private final CloudinaryDocumentUploadServiceImpl cloudinaryDocumentUploadServiceImpl;

    @Autowired
    public SubSectionController(SubSectionService subSectionService,
                          SubSectionRepository subSectionRepository,
                                CloudinaryDocumentUploadServiceImpl cloudinaryDocumentUploadServiceImpl) {
        this.subSectionService = subSectionService;
        this.subSectionRepository = subSectionRepository;
        this.cloudinaryDocumentUploadServiceImpl = cloudinaryDocumentUploadServiceImpl;
    }


    @PutMapping("/video-upload/{subSectionId}")
    @Transactional
    public ResponseEntity<SubSection>videoUpload(@RequestParam("file") MultipartFile file, @PathVariable ObjectId subSectionId){

        try{
            SubSection subSection = this.subSectionService.getSubSectionById(subSectionId);

            if(subSection!=null){
                String folder= Constant.VIDEO_FOLDER;
                String fileName=subSection.getSubSectionName();

                Map<?, ?> upload = this.cloudinaryDocumentUploadServiceImpl.upload(file, folder, fileName);

                String secureUrl = (String)upload.get("secure_url");
                Object duration = upload.get("duration");

                subSection.setVideoUrl(secureUrl);
                subSection.setTimeDuration(String.valueOf(duration));

                SubSection updtedSubSection = this.subSectionRepository.save(subSection);

                return new ResponseEntity<>(updtedSubSection, HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        }catch (InterruptedException e) {
            // Restore interrupted status
            Thread.currentThread().interrupt();
            log.error("Thread was interrupted while uploading thumbnail: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        catch (Exception e) {
            log.error("Error occurred while uploading thumbnail: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/create")
    public ResponseEntity<SubSection>createSubSection(@Valid @RequestBody SubSectionRequest subSectionRequest){

        try{
            SubSection subSection = this.subSectionService.createSubSection(subSectionRequest);

            if(subSection!=null){
                return new ResponseEntity<>(subSection, HttpStatus.CREATED);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        }catch (Exception e){
            log.error("Error occurred while creating sub-section: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


    @PutMapping("/update/{subSectionId}")
    public ResponseEntity<Boolean>updateSubSection(@RequestBody SubSectionRequest subSectionRequest, @PathVariable ObjectId subSectionId){

        try{
            boolean isUpdate = this.subSectionService.updateSubSection(subSectionRequest, subSectionId);

            if(isUpdate){
                return new ResponseEntity<>(true,HttpStatus.OK);
            }
            return new ResponseEntity<>(false,HttpStatus.NOT_FOUND);

        }catch (Exception e){
            log.error("Error occurred while updating a sub-section: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/delete/{subSectionId}/{sectionId}")
    public ResponseEntity<Boolean>deleteSubSection(@PathVariable ObjectId subSectionId,@PathVariable ObjectId sectionId){

        try{
            boolean isDelete = this.subSectionService.deleteSubSection(subSectionId,sectionId);

            if(isDelete){
                return new ResponseEntity<>(true,HttpStatus.OK);
            }
            return new ResponseEntity<>(false,HttpStatus.NOT_FOUND);

        }catch (Exception e){
            log.error("Error occurred while deleting a sub-section: {}", e.getMessage());
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
            log.error("Error occurred while get a sub-section: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

}

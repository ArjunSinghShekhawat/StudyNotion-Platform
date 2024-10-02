package in.study_notion.controllers;

import in.study_notion.models.Section;
import in.study_notion.request.SectionRequest;
import in.study_notion.services.SectionService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/instructor/section")
public class SectionController {

    private final SectionService sectionService;

    @Autowired
    public SectionController(SectionService sectionService){
        this.sectionService=sectionService;
    }

    @PostMapping("/create")
    public ResponseEntity<Section>createSection(@Valid @RequestBody SectionRequest sectionRequest){

        try{
            Section section = this.sectionService.createSection(sectionRequest);

            if(section!=null){
                return new ResponseEntity<>(section, HttpStatus.CREATED);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        }catch (Exception e){
            log.error("Error occurred while creating a section: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/update/{sectionId}")
    public ResponseEntity<Boolean>updateSection(@RequestBody SectionRequest sectionRequest, @PathVariable ObjectId sectionId){

        try{
            boolean isUpdate = this.sectionService.updateSection(sectionRequest, sectionId);

            if(isUpdate){
               return new ResponseEntity<>(true,HttpStatus.OK);
            }
            return new ResponseEntity<>(false,HttpStatus.NOT_FOUND);

        }catch (Exception e){
            log.error("Error occurred while updating a section: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/delete/{sectionId}/{courseId}")
    public ResponseEntity<Boolean>deleteSection(@PathVariable ObjectId sectionId,@PathVariable ObjectId courseId){

        try{
            boolean isDelete = this.sectionService.deleteSection(sectionId,courseId);

            if(isDelete){
                return new ResponseEntity<>(true,HttpStatus.OK);
            }
            return new ResponseEntity<>(false,HttpStatus.NOT_FOUND);

        }catch (Exception e){
            log.error("Error occurred while deleting a section: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/get/{sectionId}")
    public ResponseEntity<Section>getSection(@PathVariable ObjectId sectionId){

        try{
            Section section = this.sectionService.getSectionById(sectionId);

            if(section!=null){
                return new ResponseEntity<>(section,HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        }catch (Exception e){
            log.error("Error occurred while get a section: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


}

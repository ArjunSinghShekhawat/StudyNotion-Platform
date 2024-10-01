package in.studyNotion.controllers;

import in.studyNotion.models.Section;
import in.studyNotion.request.SectionRequest;
import in.studyNotion.services.SectionService;
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

    @Autowired
    private SectionService sectionService;

    @PostMapping("/create")
    public ResponseEntity<Section>createSection(@RequestBody SectionRequest sectionRequest){

        try{
            Section section = this.sectionService.createSection(sectionRequest);

            if(section!=null){
                return new ResponseEntity<>(section, HttpStatus.CREATED);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/update/{sectionId}")
    public ResponseEntity<Boolean>updateSection(@RequestBody SectionRequest sectionRequest, @PathVariable ObjectId sectionId){

        try{
            boolean isUpdate = this.sectionService.updateSection(sectionRequest, sectionId);

            if(isUpdate){
               return new ResponseEntity<>(isUpdate,HttpStatus.OK);
            }
            return new ResponseEntity<>(isUpdate,HttpStatus.NOT_FOUND);

        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/delete/{sectionId}/{courseId}")
    public ResponseEntity<Boolean>deleteSection(@PathVariable ObjectId sectionId,@PathVariable ObjectId courseId){

        try{
            boolean isDelete = this.sectionService.deleteSection(sectionId,courseId);

            if(isDelete){
                return new ResponseEntity<>(isDelete,HttpStatus.OK);
            }
            return new ResponseEntity<>(isDelete,HttpStatus.NOT_FOUND);

        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/get/{sectionId}")
    public ResponseEntity<Section>deleteSection(@PathVariable ObjectId sectionId){

        try{
            Section section = this.sectionService.getSectionById(sectionId);

            if(section!=null){
                return new ResponseEntity<>(section,HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


}

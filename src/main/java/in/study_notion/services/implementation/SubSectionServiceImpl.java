package in.study_notion.services.implementation;

import in.study_notion.models.Section;
import in.study_notion.models.SubSection;
import in.study_notion.repositories.SectionRepository;
import in.study_notion.repositories.SubSectionRepository;
import in.study_notion.request.SubSectionRequest;
import in.study_notion.services.SubSectionService;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Optional;

@Slf4j
@Service
public class SubSectionServiceImpl implements SubSectionService {

    private final SubSectionRepository subSectionRepository;
    private final SectionRepository sectionRepository;

    @Autowired
    public SubSectionServiceImpl(SubSectionRepository subSectionRepository, SectionRepository sectionRepository) {
        this.subSectionRepository = subSectionRepository;
        this.sectionRepository = sectionRepository;
    }

    @Override
    public SubSection createSubSection(SubSectionRequest subSectionRequest) throws Exception {

        /*
        This method is used for create subsection
         */
        SubSection saveSubSection=null;
        try{
            //create subSection
            SubSection subSection = new SubSection();
            subSection.setSubSectionName(subSectionRequest.getSubSectionName());
            subSection.setSubSectionDescription(subSectionRequest.getSubSectionDescription());

            //save subSection
            saveSubSection = this.subSectionRepository.save(subSection);

            Optional<Section> optionalSection = this.sectionRepository.findById(subSectionRequest.getSectionId());


            if(optionalSection.isPresent()){

                Section section = optionalSection.get();
                section.getSubSections().add(saveSubSection);

                this.sectionRepository.save(section);
            }
        }catch (Exception e){
            log.error("Error occurred while create a new sub section");

        }
        return saveSubSection;
    }

    @Override
    public boolean updateSubSection(SubSectionRequest subSectionRequest, ObjectId subSectionId) throws Exception {

        boolean isUpdate = false;
        try{

            /*
            This method is used for update subSection
             */

            //update subSection
            SubSection subSection = getSubSectionById(subSectionId);
            subSection.setSubSectionName(subSectionRequest.getSubSectionName()!=null && !subSectionRequest.getSubSectionName().isEmpty() ? subSectionRequest.getSubSectionName() : subSection.getSubSectionName());
            subSection.setSubSectionDescription(subSectionRequest.getSubSectionDescription()!=null && !subSectionRequest.getSubSectionDescription().isEmpty() ? subSectionRequest.getSubSectionDescription() :subSection.getSubSectionDescription());

            //save subSection
            this.subSectionRepository.save(subSection);
            isUpdate=true;
        }catch (Exception e){
            log.error("Error occurred while update a sub section {}",e.getMessage());
        }
        return isUpdate;
    }

    @Transactional
    @Override
    public boolean deleteSubSection(ObjectId subSectionId, ObjectId sectionId) throws Exception {
        boolean isDelete=false;
        try{
            /*
            This method is used for delete subSection
             */

            //fetch subSection
            SubSection subSection = getSubSectionById(subSectionId);
            Section section = this.sectionRepository.findById(sectionId).orElseThrow(() -> new Exception("Section not found !"));

            if(subSection!=null && section!=null){

                boolean sectionRemoveInCourse = section.getSubSections().removeIf(sub->sub.getId().equals(subSectionId));

                if (!sectionRemoveInCourse) {
                    throw new NoSuchElementException("In Section, Sub Section is not found");
                }
                this.sectionRepository.save(section);
                this.subSectionRepository.deleteById(subSectionId);
                isDelete=true;
            }
        }catch (Exception e){
            log.error("Error occurred while delete a sub section !");
        }
        return isDelete;
    }

    @Override
    public SubSection getSubSectionById(ObjectId subSectionId) throws Exception {
        /*
        This method is used for get subSection
         */
        return this.subSectionRepository.findById(subSectionId).orElseThrow(() -> new Exception("Sub Section not found !"));
    }
}

package in.studyNotion.services.implement;

import in.studyNotion.models.Section;
import in.studyNotion.models.SubSection;
import in.studyNotion.repositories.SectionRepository;
import in.studyNotion.repositories.SubSectionRepository;
import in.studyNotion.request.SubSectionRequest;
import in.studyNotion.services.SubSectionService;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
public class SubSectionServiceImple implements SubSectionService {

    @Autowired
    private SubSectionRepository subSectionRepository;

    @Autowired
    private SectionRepository sectionRepository;

    @Override
    public SubSection createSubSection(SubSectionRequest subSectionRequest) throws Exception {

        try{
            SubSection subSection = new SubSection();
            subSection.setSubSectionName(subSectionRequest.getSubSectionName());
            subSection.setSubSectionDescription(subSectionRequest.getSubSectionDescription());

            SubSection saveSubSection = this.subSectionRepository.save(subSection);

            Optional<Section> optionalSection = this.sectionRepository.findById(subSectionRequest.getSectionId());


            if(optionalSection.isPresent()){

                Section section = optionalSection.get();
                section.getSubSections().add(saveSubSection);

                this.sectionRepository.save(section);

                return saveSubSection;
            }
        }catch (Exception e){
            log.error("Error occurred while create a new sub section");
        }
        throw new Exception("SubSection Creation error");
    }

    @Override
    public boolean updateSubSection(SubSectionRequest subSectionRequest, ObjectId subSectionId) throws Exception {

        boolean isUpdate = false;
        try{
            SubSection subSection = getSubSectionById(subSectionId);
            subSection.setSubSectionName(subSectionRequest.getSubSectionName()!=null && !subSectionRequest.getSubSectionName().equals("")? subSectionRequest.getSubSectionName() : subSection.getSubSectionName());
            subSection.setSubSectionDescription(subSectionRequest.getSubSectionDescription()!=null && !subSectionRequest.getSubSectionDescription().equals("")? subSectionRequest.getSubSectionDescription() :subSection.getSubSectionDescription() );
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
            SubSection subSection = getSubSectionById(subSectionId);
            Section section = this.sectionRepository.findById(sectionId).orElseThrow(() -> new Exception("Section not found !"));

            if(subSection!=null && section!=null){

                boolean sectionRemoveInCourse = section.getSubSections().removeIf(sub->sub.getId().equals(subSectionId));

                if(!sectionRemoveInCourse)
                {
                    throw new Exception("In Section Sub Section is not found");
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
        return this.subSectionRepository.findById(subSectionId).orElseThrow(() -> new Exception("Sub Section not found !"));
    }
}

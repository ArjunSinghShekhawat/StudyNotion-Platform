package in.study_notion.services.implementation;

import in.study_notion.models.Course;
import in.study_notion.models.Section;
import in.study_notion.repositories.CourseRepository;
import in.study_notion.repositories.SectionRepository;
import in.study_notion.request.SectionRequest;
import in.study_notion.services.SectionService;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Slf4j
public class SectionServiceImpl implements SectionService {

    private final SectionRepository sectionRepository;
    private final CourseRepository courseRepository;

    @Autowired
    public SectionServiceImpl(SectionRepository sectionRepository, CourseRepository courseRepository) {
        this.sectionRepository = sectionRepository;
        this.courseRepository = courseRepository;
    }

    @Override
    public Section createSection(SectionRequest sectionRequest) throws Exception {

        /*
        This method is used for create section
         */
        Section saveSection=null;
        try{
            //section creation
            Section section = new Section();
            section.setSectionName(sectionRequest.getSectionName());

            saveSection = this.sectionRepository.save(section);

            Optional<Course> optionalCourse = this.courseRepository.findById(sectionRequest.getCourseId());

            if(optionalCourse.isPresent()){

                Course course = optionalCourse.get();
                course.getCourseContent().add(saveSection);

                this.courseRepository.save(course);
            }
        }catch (Exception e){
            log.error("Error occurred while create a new section {} ",e.getMessage());
        }
        return saveSection;
    }

    @Override
    public boolean updateSection(SectionRequest sectionRequest, ObjectId sectionId) {
        /*
        This method is used for update section
         */

        boolean isUpdate = false;
        try{
            //fetch section
            Section section = getSectionById(sectionId);
            section.setSectionName(sectionRequest.getSectionName()!=null && !sectionRequest.getSectionName().isEmpty() ? sectionRequest.getSectionName() : section.getSectionName());

            this.sectionRepository.save(section);
            isUpdate=true;
        }catch (Exception e){
            log.error("Error occurred while update a section {}",e.getMessage());
        }
        return isUpdate;
    }

    @Transactional
    @Override
    public boolean deleteSection(ObjectId sectionId, ObjectId courseId) {

        /*
        This method is used for delete section
         */
        boolean isDelete=false;

        try{
            Section section = getSectionById(sectionId);
            Course course = this.courseRepository.findById(courseId).orElseThrow(() -> new Exception("Course not found !"));

            if(section!=null && course!=null){

                boolean sectionRemoveInCourse = course.getCourseContent().removeIf(sec -> sec.getId().equals(sectionId));

                if(!sectionRemoveInCourse)
                {
                    throw new NoSuchFieldException("In Course Content Section is not found");
                }
                //saved course and section
                this.courseRepository.save(course);
                this.sectionRepository.deleteById(sectionId);
                isDelete=true;
            }
        }catch (Exception e){
            log.error("Error occurred while delete a section !");
        }
        return isDelete;
    }

    @Override
    public Section getSectionById(ObjectId sectionId) throws Exception {
        /*
        This method is used for get section
         */
        return this.sectionRepository.findById(sectionId).orElseThrow(() -> new Exception("Section not found !"));
    }

}

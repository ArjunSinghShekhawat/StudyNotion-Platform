package in.studyNotion.services.implement;

import in.studyNotion.models.Course;
import in.studyNotion.models.Section;
import in.studyNotion.repositories.CourseRepository;
import in.studyNotion.repositories.SectionRepository;
import in.studyNotion.request.SectionRequest;
import in.studyNotion.services.SectionService;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class SectionServiceImple implements SectionService {

    @Autowired
    private SectionRepository sectionRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Override
    public Section createSection(SectionRequest sectionRequest) throws Exception {
        try{
            Section section = new Section();
            section.setSectionName(sectionRequest.getSectionName());

            Section saveSection = this.sectionRepository.save(section);

            Optional<Course> optionalCourse = this.courseRepository.findById(sectionRequest.getCourseId());

            if(optionalCourse.isPresent()){

                Course course = optionalCourse.get();
                course.getCourseContent().add(saveSection);

                this.courseRepository.save(course);

                return saveSection;
            }
        }catch (Exception e){
            log.error("Error occurred while create a new section");
        }
        throw new Exception("Course is not found !");
    }

    @Override
    public boolean updateSection(SectionRequest sectionRequest, ObjectId sectionId) throws Exception {

        boolean isUpdate = false;
        try{
            Section section = getSectionById(sectionId);
            section.setSectionName(sectionRequest.getSectionName()!=null && !sectionRequest.getSectionName().equals("")? sectionRequest.getSectionName() : section.getSectionName());

            this.sectionRepository.save(section);
            isUpdate=true;
        }catch (Exception e){
            log.error("Error occurred while update a section {}",e.getMessage());
        }
        return isUpdate;
    }

    @Transactional
    @Override
    public boolean deleteSection(ObjectId sectionId, ObjectId courseId) throws Exception {
        boolean isDelete=false;

        try{
            Section section = getSectionById(sectionId);
            Course course = this.courseRepository.findById(courseId).orElseThrow(() -> new Exception("Course not found !"));

            if(section!=null && course!=null){

                boolean sectionRemoveInCourse = course.getCourseContent().removeIf(sec -> sec.getId().equals(sectionId));

                if(!sectionRemoveInCourse)
                {
                    throw new Exception("In Course Content Section is not found");
                }
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
        return this.sectionRepository.findById(sectionId).orElseThrow(() -> new Exception("Section not found !"));
    }

}

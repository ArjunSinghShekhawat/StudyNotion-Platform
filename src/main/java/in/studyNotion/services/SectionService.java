package in.studyNotion.services;

import in.studyNotion.models.Section;
import in.studyNotion.request.SectionRequest;
import org.bson.types.ObjectId;

import java.util.List;

public interface SectionService {

    Section createSection(SectionRequest sectionRequest) throws Exception;
    boolean updateSection(SectionRequest sectionRequest, ObjectId sectionId) throws Exception;
    boolean deleteSection(ObjectId sectionId,ObjectId courseId) throws Exception;
    Section getSectionById(ObjectId sectionId) throws Exception;

}

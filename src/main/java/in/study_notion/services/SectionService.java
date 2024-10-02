package in.study_notion.services;

import in.study_notion.models.Section;
import in.study_notion.request.SectionRequest;
import org.bson.types.ObjectId;

public interface SectionService {

    Section createSection(SectionRequest sectionRequest) throws Exception;
    boolean updateSection(SectionRequest sectionRequest, ObjectId sectionId) throws Exception;
    boolean deleteSection(ObjectId sectionId,ObjectId courseId) throws Exception;
    Section getSectionById(ObjectId sectionId) throws Exception;

}

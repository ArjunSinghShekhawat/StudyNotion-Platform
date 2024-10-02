package in.study_notion.services;

import in.study_notion.models.SubSection;
import in.study_notion.request.SubSectionRequest;
import org.bson.types.ObjectId;

public interface SubSectionService {
    SubSection createSubSection(SubSectionRequest subSectionRequest) throws Exception;
    boolean updateSubSection(SubSectionRequest subSectionRequest, ObjectId subSectionId) throws Exception;
    boolean deleteSubSection(ObjectId subSectionId,ObjectId sectionId) throws Exception;
    SubSection getSubSectionById(ObjectId subSectionId) throws Exception;
}

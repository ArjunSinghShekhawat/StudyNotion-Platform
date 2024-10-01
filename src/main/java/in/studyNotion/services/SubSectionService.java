package in.studyNotion.services;

import in.studyNotion.models.SubSection;
import in.studyNotion.request.SubSectionRequest;
import org.bson.types.ObjectId;

public interface SubSectionService {
    SubSection createSubSection(SubSectionRequest subSectionRequest) throws Exception;
    boolean updateSubSection(SubSectionRequest subSectionRequest, ObjectId subSectionId) throws Exception;
    boolean deleteSubSection(ObjectId subSectionId,ObjectId sectionId) throws Exception;
    SubSection getSubSectionById(ObjectId subSectionId) throws Exception;
}

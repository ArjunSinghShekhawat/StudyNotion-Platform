package in.study_notion.services;

import in.study_notion.models.User;
import in.study_notion.request.UserRequest;

public interface UserService {
    User updateUser(String jwt, UserRequest userRequest);
    User getUserAllInformationByEmail(String email);
}

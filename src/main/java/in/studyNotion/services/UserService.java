package in.studyNotion.services;

import in.studyNotion.models.User;
import in.studyNotion.request.UserRequest;

public interface UserService {
    User updateUser(String jwt, UserRequest userRequest);
}

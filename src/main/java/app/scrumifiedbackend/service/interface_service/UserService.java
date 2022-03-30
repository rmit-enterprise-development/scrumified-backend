package app.scrumifiedbackend.service.interface_service;

import app.scrumifiedbackend.dto.UserDto;
import app.scrumifiedbackend.dto.UserProjectDto;

import java.util.List;
import java.util.Map;

public interface UserService extends Service<UserDto> {
    Map<String, Boolean> isValidUser(String email, String password);

    List<UserDto> findAllExceptOne(Long id);

    UserProjectDto findAllProjectBelongTo(Long id);
}

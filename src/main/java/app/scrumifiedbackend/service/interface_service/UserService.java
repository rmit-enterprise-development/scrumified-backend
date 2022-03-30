package app.scrumifiedbackend.service.interface_service;

import app.scrumifiedbackend.dto.UserDto;

import java.util.List;

public interface UserService extends Service<UserDto> {
    List<UserDto> findAllExceptOne(Long id);
}

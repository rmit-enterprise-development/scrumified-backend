package app.scrumifiedbackend.service.implementation;

import app.scrumifiedbackend.dto.UserDto;
import app.scrumifiedbackend.entity.User;
import app.scrumifiedbackend.service.interface_service.UserService;

import java.util.List;

public class UserServiceImpl implements UserService {
    @Override
    public List<UserDto> findAll() {
        return null;
    }

    @Override
    public UserDto findOne(Long id) {
        return null;
    }

    @Override
    public UserDto create(UserDto input) {
        return null;
    }

    @Override
    public UserDto update(Long id, UserDto input) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public List<UserDto> findAllExceptOne(Long id) {
        return null;
    }
}

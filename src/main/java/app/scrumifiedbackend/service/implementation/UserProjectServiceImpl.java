package app.scrumifiedbackend.service.implementation;

import app.scrumifiedbackend.dto.UserProjectDto;
import app.scrumifiedbackend.entity.UserProject;
import app.scrumifiedbackend.service.interface_service.UserProjectService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserProjectServiceImpl implements UserProjectService {

    @Override
    public List<UserProjectDto> findAll() {
        return null;
    }

    @Override
    public UserProjectDto findOne(Long id) {
        return null;
    }

    @Override
    public UserProjectDto create(UserProjectDto input) {
        return null;
    }

    @Override
    public UserProjectDto update(Long id, UserProjectDto input) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }
}

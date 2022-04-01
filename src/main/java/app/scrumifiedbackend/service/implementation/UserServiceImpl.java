package app.scrumifiedbackend.service.implementation;

import app.scrumifiedbackend.dto.ProjectDto;
import app.scrumifiedbackend.dto.UserDto;
import app.scrumifiedbackend.dto.UserProjectDto;
import app.scrumifiedbackend.entity.Project;
import app.scrumifiedbackend.entity.User;
import app.scrumifiedbackend.exception.EntityNotFoundException;
import app.scrumifiedbackend.exception.EntityNotSaveException;
import app.scrumifiedbackend.repository.UserRepo;
import app.scrumifiedbackend.service.interface_service.UserService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {
    private UserRepo userRepo;
    private ModelMapper modelMapper;

    @Override
    public List<UserDto> findAll() {
        List<User> users = getAll();
        return convertToDto(users);
    }

    @Override
    public UserDto findOne(Long id) {
        User user = getById(id);
        return modelMapper.map(user, UserDto.class);
    }

    @Override
    public UserDto create(UserDto input) {
        User user = modelMapper.map(input, User.class);
        user = save(user);
        return modelMapper.map(user, UserDto.class);
    }

    @Override
    public UserDto update(Long id, UserDto input) {
        User user = getById(id);

        if (input.getFirstName() != null && !input.getFirstName().equals(user.getFirstName())) {
            user.setFirstName(input.getFirstName());
        }

        if (input.getLastName() != null && !input.getLastName().equals(user.getLastName())) {
            user.setLastName(input.getLastName());
        }

        if (input.getEmail() != null && !input.getEmail().equals(user.getEmail())) {
            user.setEmail(input.getEmail());
        }

        if (input.getPassword() != null && !input.getPassword().equals(user.getPassword())) {
            user.setPassword(input.getPassword());
        }

        user = save(user);
        return modelMapper.map(user, UserDto.class);
    }

    @Override
    public void delete(Long id) {
        User user = getById(id);
        userRepo.delete(user);
    }

    @Override
    public Map<String, Boolean> isValidUser(String email, String password) {
        Map<String, Boolean> map = new HashMap<>();
        userRepo.findByEmail(email).ifPresentOrElse((user) -> {
            if (user.getPassword().equals(password)) {
                map.put("email", true);
                map.put("password", true);
            } else {
                map.put("password", false);
            }
        }, () -> {
            map.put("email", false);
        });
        return map;
    }

    @Override
    public List<UserDto> findAllExceptOne(Long id) {
        List<User> users = userRepo.findAllExceptOne(id);
        return convertToDto(users);
    }

    @Override
    public UserProjectDto findAllProjectBelongTo(Long id) {
        User user = userRepo.getById(id);
        List<Project> ownedProject = user.getOwnedProjects();
        List<Project> participateProject = user.getParticipatedProjects();
        UserProjectDto userProjectDto = new UserProjectDto();

        List<ProjectDto> ownedProjectDto = new ArrayList<>();
        for (Project project : ownedProject) {
            ProjectDto projectDto = modelMapper.map(project, ProjectDto.class);
            projectDto.setOwnerId(project.getOwner().getId());
            projectDto.setParticipantsId(project.getParticipatedId());
            ownedProjectDto.add(projectDto);
        }
        List<ProjectDto> participateProjectDto = new ArrayList<>();
        for (Project project : participateProject) {
            ProjectDto projectDto = modelMapper.map(project, ProjectDto.class);
            projectDto.setOwnerId(project.getOwner().getId());
            projectDto.setParticipantsId(project.getParticipatedId());
            participateProjectDto.add(projectDto);
        }
        userProjectDto.setOwnedProjects(ownedProjectDto);
        userProjectDto.setParticipatedProjects(participateProjectDto);
        return userProjectDto;
    }

    private User getById(Long id) {
        return userRepo.findById(id).orElseThrow(() -> new EntityNotFoundException("ID " + id + " not exist"));
    }

    private List<User> getAll() {
        return userRepo.findAll();
    }

    private User save(User user) {
        try {
            return userRepo.save(user);
        } catch (RuntimeException e) {
            throw new EntityNotSaveException(e.getCause().getCause().getMessage());
        }
    }

    private List<UserDto> convertToDto(List<User> users) {
        List<UserDto> userDtoList = new ArrayList<>();
        for (User user : users) {
            userDtoList.add(modelMapper.map(user, UserDto.class));
        }
        return userDtoList;
    }
}

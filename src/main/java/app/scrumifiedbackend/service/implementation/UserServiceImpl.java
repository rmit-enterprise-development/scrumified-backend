package app.scrumifiedbackend.service.implementation;

import app.scrumifiedbackend.dto.PaginationDto;
import app.scrumifiedbackend.dto.UserDto;
import app.scrumifiedbackend.entity.User;
import app.scrumifiedbackend.exception.EntityNotFoundException;
import app.scrumifiedbackend.exception.EntityNotSaveException;
import app.scrumifiedbackend.mapping.ModelMapperSingleton;
import app.scrumifiedbackend.repository.UserRepo;
import app.scrumifiedbackend.service.interface_service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private UserRepo userRepo;

    private List<UserDto> convertToDto(List<User> users) {
        List<UserDto> userDtoList = new ArrayList<>();
        for (User user : users) {
            userDtoList.add(ModelMapperSingleton.modelMapper.map(user, UserDto.class));
        }
        return userDtoList;
    }

    private User getById(Long id) {
        return userRepo.findById(id).orElseThrow(() -> new EntityNotFoundException("User ID " + id + " not exist"));
    }

    private Page<User> getAll(String key, Pageable pageable) {
        return userRepo.findAll("%" + key + "%", pageable);
    }

    private User save(User user) {
        try {
            return userRepo.save(user);
        } catch (RuntimeException e) {
            throw new EntityNotSaveException(e.getCause().getCause().getMessage());
        }
    }

    private PaginationDto<List<UserDto>> getListPaginationDto(Page<User> page) {
        List<UserDto> userDtoList = new ArrayList<>();
        if (page.hasContent()) {
            List<User> users = page.getContent();
            userDtoList = convertToDto(users);
        }
        return new PaginationDto<>(userDtoList, page.getTotalElements(), page.getTotalPages(), page.getNumber());
    }

    @Override
    public PaginationDto<List<UserDto>> findAll(String key, Pageable pageable) {
        Page<User> page = getAll(key, pageable);
        return getListPaginationDto(page);
    }

    @Override
    public UserDto findOne(Long id) {
        User user = getById(id);
        return ModelMapperSingleton.modelMapper.map(user, UserDto.class);
    }

    @Override
    public UserDto create(UserDto input) {
        User user = ModelMapperSingleton.modelMapper.map(input, User.class);
        user = save(user);
        return ModelMapperSingleton.modelMapper.map(user, UserDto.class);
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

        if (input.getDescription() != null && !input.getDescription().equals(user.getDescription())) {
            user.setDescription(input.getDescription());
        }

        user = save(user);
        return ModelMapperSingleton.modelMapper.map(user, UserDto.class);
    }

    @Override
    public void delete(Long id) {
        User user = getById(id);
        userRepo.delete(user);
    }

    @Override
    public UserDto isValidUser(String email, String password) {
        UserDto userDto = null;
        Optional<User> optional = userRepo.findByEmail(email);
        if (optional.isPresent()) {
            User user = optional.get();
            userDto = ModelMapperSingleton.modelMapper.map(user, UserDto.class);
            if (user.getPassword().equals(password)) {
                userDto.setIsSuccess(true);
            } else {
                userDto.setIsSuccess(false);
                userDto.addErrorTarget("password");
            }
        } else {
            userDto = new UserDto();
            userDto.setIsSuccess(false);
            userDto.setErrorTarget(Arrays.asList("email", "password"));
        }
        return userDto;
    }

    @Override
    public PaginationDto<List<UserDto>> findAllExceptOne(Long id, String key, Pageable pageable) {
        Page<User> page = userRepo.findAllExceptOne(id, key, pageable);
        return getListPaginationDto(page);
    }
}

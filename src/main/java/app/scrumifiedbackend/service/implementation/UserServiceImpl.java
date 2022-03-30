package app.scrumifiedbackend.service.implementation;

import app.scrumifiedbackend.dto.UserDto;
import app.scrumifiedbackend.entity.User;
import app.scrumifiedbackend.exception.EntityNotSaveException;
import app.scrumifiedbackend.repository.UserRepo;
import app.scrumifiedbackend.service.interface_service.UserService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private UserRepo userRepo;
    private ModelMapper modelMapper;

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
        User user = modelMapper.map(input, User.class);
        try {
            userRepo.save(user);
            return input;
        } catch (RuntimeException e) {
            throw new EntityNotSaveException(e.getCause().getCause().getMessage());
        }
    }

    @Override
    public UserDto update(Long id, UserDto input) {
        User user = userRepo.getById(id);
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
        
        userRepo.save(user);
        return modelMapper.map(user, UserDto.class);
    }

    @Override
    public void delete(Long id) {

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
        return null;
    }
}

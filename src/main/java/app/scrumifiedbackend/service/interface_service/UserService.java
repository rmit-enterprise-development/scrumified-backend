package app.scrumifiedbackend.service.interface_service;

import app.scrumifiedbackend.dto.PaginationDto;
import app.scrumifiedbackend.dto.UserDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService extends Service<UserDto> {
    UserDto isValidUser(String email, String password);

    PaginationDto<List<UserDto>> findAllExceptOne(Long id, String key, Pageable pageable);
}

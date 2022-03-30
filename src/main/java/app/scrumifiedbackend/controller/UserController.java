package app.scrumifiedbackend.controller;

import app.scrumifiedbackend.assembler.UserDtoEntityAssembler;
import app.scrumifiedbackend.dto.ProjectDto;
import app.scrumifiedbackend.dto.UserDto;
import app.scrumifiedbackend.entity.Project;
import app.scrumifiedbackend.entity.User;
import app.scrumifiedbackend.service.interface_service.ProjectService;
import app.scrumifiedbackend.service.interface_service.UserService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping
@CrossOrigin
@AllArgsConstructor
public class UserController {
    private UserService userService;
    private ProjectService projectService;

    private UserDtoEntityAssembler userDtoEntityAssembler;

    @PostMapping("/register")
    public EntityModel<UserDto> createUser(@RequestBody UserDto userDto) {
        UserDto createdUser = userService.create(userDto);
        return userDtoEntityAssembler.toModel(createdUser);
    }

    @GetMapping("/login")
    public EntityModel<UserDto> authenticateUser(@RequestBody UserDto userDto) {
        Map<String, Boolean> map = userService.isValidUser(userDto.getEmail(), userDto.getPassword());
        userDto.setIsSuccess(true);
        for (String s : map.keySet()) {
            if (!map.get(s)) {
                userDto.setIsSuccess(false);
                userDto.addErrorTarget(s);
            }
        }
        return userDtoEntityAssembler.toModel(userDto);
    }

    @PutMapping("/users/{userId}")
    public EntityModel<UserDto> updateUser(@PathVariable("userId") Long id, @RequestBody UserDto userDto) {
        UserDto updateUser = userService.update(id, userDto);
        return userDtoEntityAssembler.toModel(updateUser);
    }

    @DeleteMapping("/users/{userId}")
    public void deleteUser(@PathVariable("userId") Long id) {
        userService.delete(id);
    }

    @GetMapping("/users/{userId}")
    public EntityModel<UserDto> getUser(@PathVariable("userId") Long id) {
        return userDtoEntityAssembler.toModel(userService.findOne(id));
    }

    @GetMapping("/users")
    public CollectionModel<EntityModel<UserDto>> getAllUsers(@RequestParam(name = "exceptedId", required = false) Long id) {
        List<UserDto> userDtoList = id == null ? userService.findAll() : userService.findAllExceptOne(id);
        return userDtoEntityAssembler.toCollectionModel(userDtoList);
    }

    @PostMapping("/users/{userId}/projects")
    public EntityModel<ProjectDto> createProject(@PathVariable("userId") Long id, @RequestBody ProjectDto projectDto) {
        return null;
    }

    @GetMapping("/users/{userId}/projects")
    public EntityModel<UserDto> getAllProject(@PathVariable("userId") Long id) {
        return null;
    }
}

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

@RestController
@RequestMapping
@CrossOrigin
@AllArgsConstructor
public class UserController {
    private ModelMapper modelMapper;
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
        return null;
    }

    @PutMapping("/users/{userId}")
    public EntityModel<UserDto> updateUser(@PathVariable("userId") Long id, @RequestBody UserDto userDto) {
        User user = modelMapper.map(userDto, User.class);
//        User updatedUser = userService.update(id, user);
//        UserDto updateUserDto = modelMapper.map(updatedUser, UserDto.class);
//        return userDtoEntityAssembler.toModel(updateUserDto);
        return null;
    }

    @DeleteMapping("/users/{userId}")
    public void deleteUser(@PathVariable("UserId") Long id) {
        userService.delete(id);
    }

    @GetMapping("/users/{userId}")
    public EntityModel<UserDto> getUser(@PathVariable("userId") Long id) {
//        User user = userService.findOne(id);
//        UserDto userDto = modelMapper.map(user, UserDto.class);
//        return userDtoEntityAssembler.toModel(userDto);
        return null;
    }

    @GetMapping("/users")
    public CollectionModel<EntityModel<UserDto>> getAllUsers(@RequestParam(name = "exceptedId", required = false) Long id) {
        List<User> users;
        if (id == null) {
//            users = userService.findAll();
        } else {
//            users = userService.findAllExceptOne(id);
        }

        List<UserDto> userDtoList = new ArrayList<>();
//        for (User user : users) {
//            userDtoList.add(modelMapper.map(user, UserDto.class));
//        }

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

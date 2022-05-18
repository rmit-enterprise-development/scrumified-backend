package app.scrumifiedbackend.controller;

import app.scrumifiedbackend.assembler.ProjectDtoEntityAssembler;
import app.scrumifiedbackend.assembler.StoryDtoEntityAssembler;
import app.scrumifiedbackend.assembler.UserDtoEntityAssembler;
import app.scrumifiedbackend.dto.PaginationDto;
import app.scrumifiedbackend.dto.ProjectDto;
import app.scrumifiedbackend.dto.StoryDto;
import app.scrumifiedbackend.dto.UserDto;
import app.scrumifiedbackend.mapping.MappingJsonValue;
import app.scrumifiedbackend.service.interface_service.ProjectService;
import app.scrumifiedbackend.service.interface_service.StoryService;
import app.scrumifiedbackend.service.interface_service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping
@CrossOrigin
@AllArgsConstructor
public class UserController {
    private UserService userService;
    private ProjectService projectService;
    private StoryService storyService;

    private UserDtoEntityAssembler userDtoEntityAssembler;
    private ProjectDtoEntityAssembler projectDtoEntityAssembler;
    private StoryDtoEntityAssembler storyDtoEntityAssembler;

    @PostMapping("/register")
    public MappingJacksonValue createUser(@RequestBody UserDto userDto) {
        UserDto createdUser = userService.create(userDto);

        HashMap<String, Set<String>> filter = new HashMap<>();
        filter.put("userFilter", UserDto.getFilter("password", "isSuccess", "errorTarget"));

        return new MappingJsonValue<>().returnJson(filter, userDtoEntityAssembler.toModel(createdUser));
    }

    @PostMapping("/login")
    public MappingJacksonValue authenticateUser(@RequestBody UserDto userDto) {
        UserDto resultUserDto = userService.isValidUser(userDto.getEmail(), userDto.getPassword());

        EntityModel<UserDto> model = userDtoEntityAssembler.toModel(resultUserDto);

        HashMap<String, Set<String>> filter = new HashMap<>();
        if (Objects.requireNonNull(model.getContent()).getIsSuccess()) {
            filter.put("userFilter", UserDto.getFilter("errorTarget"));
        } else {
            filter.put("userFilter", UserDto.getFilter(
                    "password", "id", "email", "firstName", "lastName"
            ));
        }

        return new MappingJsonValue<>().returnJson(filter, model);
    }

    @GetMapping("/users/{userId}")
    public MappingJacksonValue getUser(@PathVariable("userId") Long id) {
        EntityModel<UserDto> model = userDtoEntityAssembler.toModel(userService.findOne(id));
        HashMap<String, Set<String>> filter = new HashMap<>();
        filter.put("userFilter", UserDto.getFilter("password", "isSuccess", "errorTarget"));
        return new MappingJsonValue<>().returnJson(filter, model);
    }

    @PutMapping("/users/{userId}")
    public MappingJacksonValue updateUser(@PathVariable("userId") Long id, @RequestBody UserDto userDto) {
        UserDto updateUser = userService.update(id, userDto);
        EntityModel<UserDto> model = userDtoEntityAssembler.toModel(updateUser);
        HashMap<String, Set<String>> filter = new HashMap<>();
        filter.put("userFilter", UserDto.getFilter("password", "isSuccess", "errorTarget"));
        return new MappingJsonValue<>().returnJson(filter, model);
    }

    @DeleteMapping("/users/{userId}")
    public void deleteUser(@PathVariable("userId") Long id) {
        userService.delete(id);
    }

    @GetMapping("/users")
    public MappingJacksonValue getAllUsers(
            @RequestParam(name = "exceptedId", required = false) Long id,
            @RequestParam(name = "key", defaultValue = "", required = false) String key,
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "limit", defaultValue = "4", required = false) int limit
    ) {
        Pageable pageable = PageRequest.of(page, limit);
        PaginationDto<List<UserDto>> paginationDto = id == null ?
                userService.findAll(key, pageable) :
                userService.findAllExceptOne(id, key, pageable);
        CollectionModel<EntityModel<UserDto>> collectionModel =
                userDtoEntityAssembler.toCollectionModel(paginationDto.getEntity());

        HashMap<String, Set<String>> filter = new HashMap<>();
        filter.put("paginationFilter", PaginationDto.getFilter());
        filter.put("userFilter", UserDto.getFilter("password", "isSuccess", "errorTarget"));

        return new MappingJsonValue<>().returnJson(filter, collectionModel);
    }

    @PostMapping("/users/{userId}/projects")
    public MappingJacksonValue createProject(@PathVariable("userId") Long id, @RequestBody ProjectDto projectDto) {
        projectDto.setOwnerId(id);
        ProjectDto createdProject = projectService.create(projectDto);

        HashMap<String, Set<String>> filter = new HashMap<>();
        filter.put("userFilter", UserDto.getFilter("password", "isSuccess", "errorTarget"));
        filter.put("projectFilter", ProjectDto.getFilter(
                "totalPoints", "todoPoints",
                "inProgressPoints", "donePoints")
        );
        return new MappingJsonValue<>().returnJson(filter, projectDtoEntityAssembler.toModel(createdProject));
    }

    @GetMapping("/users/{userId}/projects")
    public MappingJacksonValue getAllProjectBelongToUser(
            @PathVariable("userId") Long id,
            @RequestParam(name = "key", defaultValue = "", required = false) String key,
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "limit", defaultValue = "4", required = false) int limit,
            @RequestParam(name = "sortProp", defaultValue = "id", required = false) String sortProp,
            @RequestParam(name = "ascending", defaultValue = "false", required = false) boolean isAscending
    ) {
        Pageable pageable;
        if (isAscending) {
            pageable = PageRequest.of(page, limit, Sort.by(sortProp).ascending());
        } else {
            pageable = PageRequest.of(page, limit, Sort.by(sortProp).descending());
        }
        PaginationDto<List<ProjectDto>> paginationDto = projectService.findProjectBelongToUser(id, key, pageable);
        PaginationDto<CollectionModel<EntityModel<ProjectDto>>> resultPaginationDto = new PaginationDto<>(
                projectDtoEntityAssembler.toCollectionModel(paginationDto.getEntity()),
                paginationDto.getTotalElements(),
                paginationDto.getTotalPages(),
                paginationDto.getCurrentPage()
        );

        HashMap<String, Set<String>> filter = new HashMap<>();
        filter.put("paginationFilter", PaginationDto.getFilter());
        filter.put("userFilter", UserDto.getFilter("password", "isSuccess", "errorTarget"));
        filter.put("projectFilter", ProjectDto.getFilter(
                "totalPoints", "todoPoints",
                "inProgressPoints", "donePoints")
        );

        return new MappingJsonValue<>().returnJson(filter, resultPaginationDto);
    }

    @GetMapping("users/{userId}/stories")
    public MappingJacksonValue getAllStoriesBelongToUser(
            @PathVariable("userId") Long id,
            @RequestParam(name = "key", defaultValue = "", required = false) String key,
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "limit", defaultValue = "4", required = false) int limit,
            @RequestParam(name = "sortProp", defaultValue = "id", required = false) String sortProp,
            @RequestParam(name = "ascending", defaultValue = "false", required = false) boolean isAscending,
            @RequestParam(name = "projectId", required = false) Long projectId,
            @RequestParam(name = "exceptStatus", defaultValue = "", required = false) String exceptStatus
    ) {

        Pageable pageable = isAscending ?
                PageRequest.of(page, limit, Sort.by(sortProp).ascending()) :
                PageRequest.of(page, limit, Sort.by(sortProp).descending());

        PaginationDto<List<StoryDto>> paginationDto = storyService
                .findStoriesBelongToUserAndProject(
                        id, key, pageable, projectId, exceptStatus
                );
        PaginationDto<CollectionModel<EntityModel<StoryDto>>> resultPaginationDto = new PaginationDto<>(
                storyDtoEntityAssembler.toCollectionModel(paginationDto.getEntity()),
                paginationDto.getTotalElements(),
                paginationDto.getTotalPages(),
                paginationDto.getCurrentPage()
        );

        HashMap<String, Set<String>> filter = new HashMap<>();
        filter.put("paginationFilter", PaginationDto.getFilter());
        filter.put("userFilter", UserDto.getFilter("password", "isSuccess", "errorTarget"));
        filter.put("storyFilter", StoryDto.getFilter());

        return new MappingJsonValue<>().returnJson(filter, resultPaginationDto);
    }
}

package app.scrumifiedbackend.controller;

import app.scrumifiedbackend.assembler.ProjectDtoEntityAssembler;
import app.scrumifiedbackend.assembler.SprintDtoEntityAssembler;
import app.scrumifiedbackend.assembler.StoryDtoEntityAssembler;
import app.scrumifiedbackend.dto.*;
import app.scrumifiedbackend.mapping.MappingJsonValue;
import app.scrumifiedbackend.service.interface_service.ProjectService;
import app.scrumifiedbackend.service.interface_service.SprintService;
import app.scrumifiedbackend.service.interface_service.StoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Tag(name = "Project")
@RestController
@RequestMapping
@CrossOrigin
@AllArgsConstructor
public class ProjectController {
    private ProjectService projectService;
    private ProjectDtoEntityAssembler projectDtoEntityAssembler;

    private StoryService storyService;
    private StoryDtoEntityAssembler storyDtoEntityAssembler;

    private SprintService sprintService;
    private SprintDtoEntityAssembler sprintDtoEntityAssembler;

    @GetMapping("/projects")
    public MappingJacksonValue getAllProjects(
            @RequestParam(name = "key", defaultValue = "", required = false) String key,
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "limit", defaultValue = "4", required = false) int limit,
            @RequestParam(name = "sortProp", defaultValue = "id", required = false) String sortProp,
            @RequestParam(name = "ascending", defaultValue = "false", required = false) boolean isAscending
    ) {
        Pageable pageable = isAscending ?
                PageRequest.of(page, limit, Sort.by(sortProp).ascending()) :
                PageRequest.of(page, limit, Sort.by(sortProp).descending());

        PaginationDto<List<ProjectDto>> paginationDto = projectService.findAll(key, pageable);
        CollectionModel<EntityModel<ProjectDto>> collectionModel =
                projectDtoEntityAssembler.toCollectionModel(paginationDto.getEntity());
        PaginationDto<CollectionModel<EntityModel<ProjectDto>>> resultPaginationDto = new PaginationDto<>(
                collectionModel,
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

    @Operation(summary = "This is to fetch all the projects in the database")
    @ApiResponse(content = @Content(array = @ArraySchema(schema = @Schema(implementation = ProjectDto.class))))
    @GetMapping("/projects/{projectId}")
    public MappingJacksonValue getProject(
            @PathVariable("projectId") Long id,
            @RequestParam(name = "getPoints", defaultValue = "false", required = false) Boolean requiredPoints
    ) {
        EntityModel<ProjectDto> model;
        if (requiredPoints) {
            model = projectDtoEntityAssembler.toModel(projectService.findProjectWithPoints(id));
        } else {
            model = projectDtoEntityAssembler.toModel(projectService.findOne(id));
        }

        HashMap<String, Set<String>> filter = new HashMap<>();
        filter.put("userFilter", UserDto.getFilter("password", "isSuccess", "errorTarget"));
        filter.put("projectFilter", ProjectDto.getFilter(
                "totalPoints", "todoPoints",
                "inProgressPoints", "donePoints")
        );
        return new MappingJsonValue<>().returnJson(filter, model);
    }

    @PutMapping("/projects/{projectId}")
    public MappingJacksonValue updateProject(@PathVariable("projectId") Long id, @RequestBody ProjectDto projectDto) {
        ProjectDto updateProject = projectService.update(id, projectDto);

        EntityModel<ProjectDto> model = projectDtoEntityAssembler.toModel(updateProject);

        HashMap<String, Set<String>> filter = new HashMap<>();
        filter.put("userFilter", UserDto.getFilter("password", "isSuccess", "errorTarget"));
        filter.put("projectFilter", ProjectDto.getFilter(
                "totalPoints", "todoPoints",
                "inProgressPoints", "donePoints")
        );
        return new MappingJsonValue<>().returnJson(filter, model);
    }

    @DeleteMapping("/projects/{projectId}")
    public void deleteProject(@PathVariable("projectId") Long id) {
        projectService.delete(id);
    }

    @PostMapping("/projects/{projectId}/stories")
    public MappingJacksonValue createStory(@PathVariable("projectId") Long id, @RequestBody StoryDto storyDto) {
        storyDto.setProjectId(id);
        StoryDto createdStoryDto = storyService.create(storyDto);
        EntityModel<StoryDto> model = storyDtoEntityAssembler.toModel(createdStoryDto);
        HashMap<String, Set<String>> filter = new HashMap<>();
        filter.put("storyFilter", StoryDto.getFilter());
        if (id != null) {
            SseSingletonController.notifySubscriber(id, "update");
        }
        return new MappingJsonValue<>().returnJson(filter, model);
    }

    @GetMapping("/projects/{projectId}/stories")
    public MappingJacksonValue getAllStories(
            @PathVariable("projectId") Long id,
            @RequestParam(name = "isBacklog", defaultValue = "true", required = false) boolean isBacklog,
            @RequestParam(name = "key", defaultValue = "", required = false) String key,
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "limit", defaultValue = "100000000", required = false) int limit,
            @RequestParam(name = "sortProp", defaultValue = "id", required = false) String sortProp,
            @RequestParam(name = "ascending", defaultValue = "true", required = false) boolean isAscending,
            @RequestParam(name = "category", defaultValue = "", required = false) String category,
            @RequestParam(name = "returnArray", defaultValue = "true", required = false) boolean returnArray
    ) {
        Pageable pageable = isAscending ?
                PageRequest.of(page, limit, Sort.by(sortProp).ascending()) :
                PageRequest.of(page, limit, Sort.by(sortProp).descending());

        List<StoryDto> allStories = isBacklog ?
                storyService.findStoriesBelongToProjectWithBacklog(id, key, category, pageable) :
                storyService.findStoriesBelongToProject(id, key, category, pageable);
        HashMap<String, Set<String>> filter = new HashMap<>();
        filter.put("storyFilter", StoryDto.getFilter());

        if (returnArray) {
            return new MappingJsonValue<>().returnJson(filter, storyDtoEntityAssembler.toCollectionModel(allStories));
        } else {
            Map<Long, EntityModel<StoryDto>> model = new LinkedHashMap<>();
            for (StoryDto storyDto : allStories) {
                model.put(storyDto.getId(), storyDtoEntityAssembler.toModel(storyDto));
            }
            return new MappingJsonValue<>().returnJson(filter, model);
        }
    }

    @PostMapping("/projects/{projectId}/sprints")
    public MappingJacksonValue createSprint(@PathVariable("projectId") Long id, @RequestBody SprintDto sprintDto) {
        sprintDto.setProjectId(id);
        SprintDto createdSprint = sprintService.create(sprintDto);
        EntityModel<SprintDto> model = sprintDtoEntityAssembler.toModel(createdSprint);
        HashMap<String, Set<String>> filter = new HashMap<>();
        filter.put("sprintFilter", SprintDto.getFilter());

        if (id != null) {
            SseSingletonController.notifySubscriber(id, "update");
        }

        return new MappingJsonValue<>().returnJson(filter, model);
    }

    @GetMapping("/projects/{projectId}/sprints")
    public MappingJacksonValue getAllSprints(
            @PathVariable("projectId") Long id,
            @RequestParam(name = "includePercentage", defaultValue = "false", required = false) boolean includePercentage
    ) {
        if (includePercentage) {
            HashMap<String, Set<String>> filter = new HashMap<>();
            filter.put("sprintFilter", SprintDto.getFilter());
            return new MappingJsonValue<>().returnJson(filter,
                    sprintService.findAllSprintBelongToProject(id, includePercentage));
        }

        List<SprintDto> sprintDtoList = sprintService.findAllSprintBelongToProject(id, includePercentage);
        CollectionModel<EntityModel<SprintDto>> collectionModel =
                sprintDtoEntityAssembler.toCollectionModel(sprintDtoList);
        HashMap<String, Set<String>> filter = new HashMap<>();
        filter.put("sprintFilter", SprintDto.getFilter());
        return new MappingJsonValue<>().returnJson(filter, collectionModel);
    }

    @GetMapping("projects/{projectId}/sprints/current")
    public MappingJacksonValue getCurrentSprint(@PathVariable(name = "projectId") Long projectId) {
        SprintDto sprintDto = sprintService.getCurrentSprint(projectId);
        if (sprintDto == null) {
            return null;
        }
        sprintDtoEntityAssembler.toModel(sprintDto);
        HashMap<String, Set<String>> filter = new HashMap<>();
        filter.put("sprintFilter", SprintDto.getFilter());
        return new MappingJsonValue<>().returnJson(filter, sprintDtoEntityAssembler.toModel(sprintDto));
    }
}

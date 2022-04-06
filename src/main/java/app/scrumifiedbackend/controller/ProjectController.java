package app.scrumifiedbackend.controller;

import app.scrumifiedbackend.assembler.ProjectDtoEntityAssembler;
import app.scrumifiedbackend.assembler.SprintDtoEntityAssembler;
import app.scrumifiedbackend.assembler.StoryDtoEntityAssembler;
import app.scrumifiedbackend.dto.ProjectDto;
import app.scrumifiedbackend.dto.SprintDto;
import app.scrumifiedbackend.dto.StoryDto;
import app.scrumifiedbackend.service.interface_service.ProjectService;
import app.scrumifiedbackend.service.interface_service.SprintService;
import app.scrumifiedbackend.service.interface_service.StoryService;
import lombok.AllArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/projects/{projectId}")
    public EntityModel<ProjectDto> getProject(@PathVariable("projectId") Long id) {
        return projectDtoEntityAssembler.toModel(projectService.findOne(id));
    }

    @PutMapping("/projects/{projectId}")
    public EntityModel<ProjectDto> updateProject(@PathVariable("projectId") Long id, @RequestBody ProjectDto projectDto) {
        ProjectDto updateProject = projectService.update(id, projectDto);
        return projectDtoEntityAssembler.toModel(updateProject);
    }

    @DeleteMapping("/projects/{projectId}")
    public void deleteProject(@PathVariable("projectId") Long id) {
        projectService.delete(id);
    }

    @PostMapping("/projects/{projectId}/stories")
    public EntityModel<StoryDto> createStory(@PathVariable("projectId") Long id, @RequestBody StoryDto storyDto) {
        storyDto.setProjectId(id);
        StoryDto createdStoryDto = storyService.create(storyDto);
        return storyDtoEntityAssembler.toModel(createdStoryDto);
    }

    @GetMapping("/projects/{projectId}/stories")
    public CollectionModel<EntityModel<StoryDto>> getAllStories(@PathVariable("projectId") Long id) {
        List<StoryDto> allStories = storyService.findAllStoriesBelongToProject(id);
        return storyDtoEntityAssembler.toCollectionModel(allStories);
    }

    @PostMapping("/projects/{projectId}/sprints")
    public EntityModel<SprintDto> createSprint(@PathVariable("projectId") Long id, @RequestBody SprintDto sprintDto) {
        sprintDto.setProjectId(id);
        SprintDto createdSprint = sprintService.create(sprintDto);
        return sprintDtoEntityAssembler.toModel(createdSprint);
    }

    @GetMapping("/projects/{projectId}/sprints")
    public CollectionModel<EntityModel<SprintDto>> getAllSprints(@PathVariable("projectId") Long id) {
        List<SprintDto> sprintDtoList = sprintService.findAllSprintBelongToProject(id);
        return sprintDtoEntityAssembler.toCollectionModel(sprintDtoList);
    }
}

package app.scrumifiedbackend.controller;

import app.scrumifiedbackend.assembler.ProjectDtoEntityAssembler;
import app.scrumifiedbackend.assembler.StoryDtoEntityAssembler;
import app.scrumifiedbackend.dto.ProjectDto;
import app.scrumifiedbackend.dto.StoryDto;
import app.scrumifiedbackend.repository.ProjectRepo;
import app.scrumifiedbackend.service.interface_service.ProjectService;
import app.scrumifiedbackend.service.interface_service.StoryService;
import lombok.AllArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
@CrossOrigin
@AllArgsConstructor
public class ProjectController {
    private ProjectService projectService;
    private ProjectDtoEntityAssembler projectDtoEntityAssembler;
    private StoryService storyService;
    private StoryDtoEntityAssembler storyDtoEntityAssembler;

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
    public EntityModel<ProjectDto> getAllStories(@PathVariable("projectId") Long id) {
        return null;
    }
}

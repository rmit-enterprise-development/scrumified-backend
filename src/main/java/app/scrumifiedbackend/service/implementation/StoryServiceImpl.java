package app.scrumifiedbackend.service.implementation;

import app.scrumifiedbackend.dto.StoryDto;
import app.scrumifiedbackend.entity.Project;
import app.scrumifiedbackend.entity.Story;
import app.scrumifiedbackend.entity.User;
import app.scrumifiedbackend.exception.EntityNotFoundException;
import app.scrumifiedbackend.exception.EntityNotSaveException;
import app.scrumifiedbackend.repository.ProjectRepo;
import app.scrumifiedbackend.repository.StoryRepo;
import app.scrumifiedbackend.repository.UserRepo;
import app.scrumifiedbackend.service.interface_service.StoryService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping
@CrossOrigin
@AllArgsConstructor
public class StoryServiceImpl implements StoryService {
    private StoryRepo storyRepo;
    private ProjectRepo projectRepo;
    private UserRepo userRepo;

    private ModelMapper modelMapper;

    @Override
    public List<StoryDto> findAll() {
        List<Story> stories = getAll();
        return convertToDto(stories);
    }

    @Override
    public StoryDto findOne(Long id) {
        Story story = getByStoryId(id);
        return returnDto(story);
    }

    @Override
    public StoryDto create(StoryDto input) {
        Story story = modelMapper.map(input, Story.class);
        Project project = projectRepo.getById(input.getProjectId());
        story.setProject(project);
        story.setAssignee(userRepo.getById(input.getAssignId()));
        story = storyRepo.save(story);
        input.setId(story.getId());
        return input;
    }

    @Override
    public StoryDto update(Long id, StoryDto input) {
        Story story = getByStoryId(id);
        if (story.getUserStory() != null && !input.getUserStory().equals(story.getUserStory())) {
            story.setUserStory(input.getUserStory());
        }

        if (story.getCategory() != null && !input.getCategory().equals(story.getCategory())) {
            story.setCategory(input.getCategory());
        }

        if (story.getAssignee() != null && !input.getAssignId().equals(story.getAssignee().getId())) {
            User assignee = getById(input.getAssignId());
            story.setAssignee(assignee);
        }

        if (story.getPoint() != null && !input.getPoint().equals(story.getPoint())) {
            story.setPoint(input.getPoint());
        }

        if (story.getStatus() != null && !input.getStatus().equals(story.getStatus())) {
            story.setStatus(input.getStatus());
        }

        story = save(story);

        return returnDto(story);
    }

    @Override
    public void delete(Long id) {
        Story story = getByStoryId(id);
        storyRepo.delete(story);
    }

    @Override
    public List<StoryDto> findAllStoriesBelongToProject(Long id) {
        List<StoryDto> storyDtoList = new ArrayList<>();
        for (Story story : getAll()) {
            if (Objects.equals(story.getProject().getId(), id)) {
                storyDtoList.add(returnDto(story));
            }
        }
        return storyDtoList;
    }

    private Story getByStoryId(Long id) {
        try {
            return storyRepo.getById(id);
        } catch (EntityNotFoundException e) {
            throw new EntityNotSaveException("Story ID " + id + "is not found");
        }
    }

    private User getById(Long id) {
        try {
            return userRepo.getById(id);
        } catch (EntityNotFoundException e) {
            throw new EntityNotSaveException("User ID " + id + "is not found");
        }
    }

    private List<Story> getAll() {
        return storyRepo.findAll();
    }

    private Story save (Story story) {
        try {
            return storyRepo.save(story);
        } catch (RuntimeException e) {
            throw new EntityNotSaveException(e.getCause().getMessage());
        }
    }

    private List<StoryDto> convertToDto (List<Story> stories) {
        List<StoryDto> storyDtoList = new ArrayList<>();

        for (Story story : stories) {
            storyDtoList.add(returnDto(story));
        }

        return storyDtoList;
    }

    private StoryDto returnDto (Story story) {
        StoryDto storyDto = modelMapper.map(story, StoryDto.class);
        storyDto.setProjectId(story.getProject().getId());
        storyDto.setAssignId(story.getAssignee().getId());
        return storyDto;
    }
}

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

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class StoryServiceImpl implements StoryService {
    private StoryRepo storyRepo;
    private ProjectRepo projectRepo;
    private UserRepo userRepo;

    private ModelMapper modelMapper;



    @Override
    public List<StoryDto> findAll() {
        List<Story> stories = getAll();
        return convertToDo(stories);
    }

    @Override
    public StoryDto findOne(Long id) {
        Story story = getByStoryId(id);
        return modelMapper.map(story, StoryDto.class);
    }

    @Override
    public StoryDto create(StoryDto input) {
        Story story = modelMapper.map(input, Story.class);
        Project project = projectRepo.getById(input.getProjectId());
        story.setProject(project);
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
        return modelMapper.map(story, StoryDto.class);
    }

    @Override
    public void delete(Long id) {
        Story story = getByStoryId(id);
        storyRepo.delete(story);
    }

    private Story getByStoryId(Long id) {
        try {
            return storyRepo.getById(id);
        } catch (EntityNotFoundException e) {
            throw new EntityNotSaveException(e.getMessage());
        }
    }

    private User getById(Long id) {
        try {
            return userRepo.getById(id);
        } catch (EntityNotFoundException e) {
            throw new EntityNotSaveException(e.getMessage());
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

    private List<StoryDto> convertToDo (List<Story> stories) {
        List<StoryDto> storyDtoList = new ArrayList<>();

        for (Story story : stories) {
            storyDtoList.add(modelMapper.map(story, StoryDto.class));
        }

        return storyDtoList;
    }
}

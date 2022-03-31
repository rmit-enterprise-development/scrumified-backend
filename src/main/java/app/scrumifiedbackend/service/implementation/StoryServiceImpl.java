package app.scrumifiedbackend.service.implementation;

import app.scrumifiedbackend.dto.StoryDto;
import app.scrumifiedbackend.entity.Story;
import app.scrumifiedbackend.entity.User;
import app.scrumifiedbackend.exception.EntityNotFoundException;
import app.scrumifiedbackend.exception.EntityNotSaveException;
import app.scrumifiedbackend.repository.StoryRepo;
import app.scrumifiedbackend.repository.UserRepo;
import app.scrumifiedbackend.service.interface_service.StoryService;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;

@Service
public class StoryServiceImpl implements StoryService {
    private StoryRepo storyRepo;

    private ModelMapper modelMapper;

    private UserRepo userRepo;

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
        story = save(story);
        return modelMapper.map(story, StoryDto.class);
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
        return storyRepo.findById(id).orElseThrow(() -> new EntityNotFoundException("Story ID" + id + "not found"));
    }

    private User getById(Long id) {
        return userRepo.findById(id).orElseThrow(() -> new EntityNotFoundException("ID" + id + "not found"));
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

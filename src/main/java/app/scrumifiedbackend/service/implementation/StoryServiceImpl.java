package app.scrumifiedbackend.service.implementation;

import app.scrumifiedbackend.dto.PaginationDto;
import app.scrumifiedbackend.dto.StoryDto;
import app.scrumifiedbackend.entity.Project;
import app.scrumifiedbackend.entity.Sprint;
import app.scrumifiedbackend.entity.Story;
import app.scrumifiedbackend.entity.User;
import app.scrumifiedbackend.exception.EntityNotFoundException;
import app.scrumifiedbackend.exception.EntityNotSaveException;
import app.scrumifiedbackend.exception.InvalidArgumentException;
import app.scrumifiedbackend.mapping.ModelMapperSingleton;
import app.scrumifiedbackend.repository.ProjectRepo;
import app.scrumifiedbackend.repository.SprintRepo;
import app.scrumifiedbackend.repository.StoryRepo;
import app.scrumifiedbackend.repository.UserRepo;
import app.scrumifiedbackend.service.interface_service.StoryService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping
@CrossOrigin
@AllArgsConstructor
public class StoryServiceImpl implements StoryService {
    private StoryRepo storyRepo;
    private SprintRepo sprintRepo;
    private ProjectRepo projectRepo;
    private UserRepo userRepo;

    private PaginationDto<List<StoryDto>> getListPaginationDto(Page<Story> page) {
        List<StoryDto> storyDtoList = new ArrayList<>();
        if (page.hasContent()) {
            List<Story> stories = page.getContent();
            storyDtoList = convertToDto(stories);
        }
        return new PaginationDto<>(storyDtoList, page.getTotalElements(), page.getTotalPages(), page.getNumber());
    }

    private Story getByStoryId(Long id) throws EntityNotFoundException {
        return storyRepo.findById(id).orElseThrow(() -> new EntityNotFoundException("Story ID " + id + " not exist"));
    }

    private User getByUserId(Long id) {
        return userRepo.findById(id).orElseThrow(() -> new EntityNotFoundException("User ID " + id + " not exist"));
    }

    private Sprint getBySprintId(Long id) {
        return sprintRepo.findById(id).orElseThrow(() -> new EntityNotFoundException("Sprint Id " + id + " not exist"));
    }

    private Story save(Story story) {
        try {
            return storyRepo.save(story);
        } catch (RuntimeException e) {
            throw new EntityNotSaveException(e.getCause().getMessage());
        }
    }

    private List<StoryDto> convertToDto(List<Story> stories) {
        List<StoryDto> storyDtoList = new ArrayList<>();

        for (Story story : stories) {
            storyDtoList.add(ModelMapperSingleton.modelMapper.map(story, StoryDto.class));
        }
        return storyDtoList;
    }

    @Override
    public PaginationDto<List<StoryDto>> findAll(String key, Pageable pageable) {
        Page<Story> page = storyRepo.findStoriesByUserStoryContaining(key, pageable);
        return getListPaginationDto(page);
    }

    @Override
    public StoryDto findOne(Long id) {
        Story story = getByStoryId(id);
        return ModelMapperSingleton.modelMapper.map(story, StoryDto.class);
    }

    @Override
    public StoryDto create(StoryDto input) {
        Story story = ModelMapperSingleton.modelMapper.map(input, Story.class);
        story.setCreatedDate(Instant.now().getEpochSecond());
        if (storyRepo.countStoriesByStatusLikeAndProjectIdIs("backlog", input.getProjectId()) == 0) {
            story.setParentStory(null);
        } else {
            Story parentStory = storyRepo.findStoryByChildStoryIsNullAndStatusLikeAndProjectIdIs("backlog",
                    input.getProjectId());
            parentStory.setChildStory(story);
            story.setParentStory(parentStory);
        }

        story.setChildStory(null);
        story.setStatus("backlog");

        Project project = projectRepo.getById(input.getProjectId());
        story.setProject(project);

        story.setAssignee(userRepo.getById(input.getAssignId()));
        story = storyRepo.save(story);

        return ModelMapperSingleton.modelMapper.map(story, StoryDto.class);
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
            User assignee = getByUserId(input.getAssignId());
            story.setAssignee(assignee);
        }

        if (story.getPoint() != null && !input.getPoint().equals(story.getPoint())) {
            story.setPoint(input.getPoint());
        }

        if (!input.getDefOfDone().equals(story.getDefOfDone())) {
            story.setDefOfDone(input.getDefOfDone());
        }

        if (story.getStatus() != null && !input.getStatus().equals(story.getStatus())) {
            story.setStatus(input.getStatus());
        }

        story = save(story);

        return ModelMapperSingleton.modelMapper.map(story, StoryDto.class);
    }

    @Override
    public void delete(Long id) {
        Story story = getByStoryId(id);
        Story parent = story.getParentStory();
        Story child = story.getChildStory();
        if (parent != null) {
            parent.setChildStory(child);
        }
        if (child != null) {
            child.setParentStory(parent);
        }
        storyRepo.delete(story);
    }

    @Override
    public List<StoryDto> findAllStoriesBelongToSprint(Long id) {
        List<StoryDto> storyDtoList = new ArrayList<>();
        List<Story> storyList = storyRepo.findAllBySprintIdOrderByParentStoryId(id);
        for (Story story : storyList) {
            storyDtoList.add(ModelMapperSingleton.modelMapper.map(story, StoryDto.class));
        }
        return storyDtoList;
    }

    @Override
    public PaginationDto<List<StoryDto>> findStoriesBelongToUserAndProject(Long id, String key, Pageable pageable, Long projectId, String exceptStatus) {
        Page<Story> page;
        if (projectId == null) {
            page = storyRepo.findStoriesByAssigneeIdAndUserStoryContaining(id, "%" + key + "%", exceptStatus, pageable);
        } else {
            page = storyRepo.findStoriesByAssigneeIdAndProjectIdIsAndUserStoryContaining(
                    id, "%" + key + "%", pageable, exceptStatus, projectId
            );
        }
        return getListPaginationDto(page);
    }

    private void connectParentAndChild(Story parent, Story child) {
        if (parent != null) {
            parent.setChildStory(child);
        }
        if (child != null) {
            child.setParentStory(parent);
        }
    }

    private boolean isSameColumn(String currentStatus, String targetStatus) {
        return currentStatus.equals(targetStatus);
    }

    private void insertBelowDest(Story src, Story dest) {
        src.setChildStory(dest.getChildStory());
        dest.setChildStory(src);
        src.setParentStory(dest);
        Story childOfSrc = src.getChildStory();
        if (childOfSrc != null) {
            childOfSrc.setParentStory(src);
        }
    }

    private void insertOnTopDest(Story src, Story dest) {
        src.setParentStory(dest.getParentStory());
        dest.setParentStory(src);
        src.setChildStory(dest);
        Story parentOfSrc = src.getParentStory();
        if (parentOfSrc != null) {
            parentOfSrc.setChildStory(src);
        }
    }

    private void insertBottomDnd(Story src) {
        Story lastElement = storyRepo
                .findStoryByChildStoryIsNullAndStatusLikeAndProjectIdIs(
                        src.getStatus(),
                        src.getProject().getId()
                );
        if (lastElement != null) {
            System.out.println("x");
            src.setParentStory(lastElement);
            src.setChildStory(null);
            lastElement.setChildStory(src);
        } else {
            System.out.println("y");
            src.setParentStory(null);
            src.setChildStory(null);
        }
    }

    private void insertTopDnd(Story src, Story dest) {
        src.setChildStory(dest);
        src.setParentStory(null);
        dest.setParentStory(src);
    }

    private void appendToNewColumn(Story story) {
        story.setParentStory(null);
        story.setChildStory(null);
    }

    @Override
    public StoryDto dragUpdate(Long id, StoryDto storyDto, Boolean isTopDown) {
        Story story = getByStoryId(id);
        connectParentAndChild(story.getParentStory(), story.getChildStory());
        if (isSameColumn(story.getStatus(), storyDto.getStatus())) {
            Story dest = getByStoryId(storyDto.getReplaceStoryId());
            if (isTopDown) {
                insertBelowDest(story, dest);
            } else {
                insertOnTopDest(story, dest);
            }
        } else {
            if (storyDto.getSprintId() != null && storyDto.getStatus().equals("todo")) {
                Sprint sprint = getBySprintId(storyDto.getSprintId());
                story.setSprint(sprint);
            } else if (storyDto.getSprintId() == null && storyDto.getStatus().equals("backlog")) {
                story.setSprint(null);
            } else if (!(storyDto.getSprintId() == null && !storyDto.getStatus().equals("backlog") && story.getSprint() != null)) {
                throw new InvalidArgumentException("If you want to move story to a sprint then provide sprint id and " +
                        "todo status (optional replaceStoryId). If you want to move the story around the sprint " +
                        "then provide only the status and replaceStoryId");
            }
            story.setStatus(storyDto.getStatus());
            if (storyRepo.countStoriesByStatusLikeAndProjectIdIs(storyDto.getStatus(), story.getProject().getId()) == 0) {
                System.out.println("a");
                appendToNewColumn(story);
            } else {
                Story dest = storyDto.getReplaceStoryId() == null ? null : getByStoryId(storyDto.getReplaceStoryId());
                if (dest != null && dest.getParentStory() == null) {
                    System.out.println("b");
                    insertTopDnd(story, dest);
                } else if (dest == null) {
                    System.out.println("c");
                    insertBottomDnd(story);
                } else {
                    System.out.println("d");
                    insertOnTopDest(story, dest);
                }
            }
        }
        storyRepo.save(story);
        return ModelMapperSingleton.modelMapper.map(story, StoryDto.class);
    }

    @Override
    public List<StoryDto> findStoriesBelongToProjectWithBacklog(
            Long projectId, String key, String category, Pageable pageable
    ) {
        Page<Story> page = Objects.equals(category, "") ?
                storyRepo.findStoriesByProjectIdIsAndSprintIdIsNullAndUserStoryContaining(
                projectId, "%" + key + "%", pageable
        ) : storyRepo.findStoriesByProjectIdIsAndSprintIdIsNullAndUserStoryContainingAndCategoryLike(
                projectId, "%" + key + "%", category, pageable
        );

        List<StoryDto> storyDtoList = new ArrayList<>();
        if (page.hasContent()) {
            List<Story> stories = page.getContent();
            storyDtoList = convertToDto(stories);
        }

        return storyDtoList;
    }

    @Override
    public List<StoryDto> findStoriesBelongToProject(Long projectId, String key, String category, Pageable pageable) {
        Page<Story> page = Objects.equals(category, "") ?
                storyRepo.findStoriesByProjectIdIsAndUserStoryContaining(
                        projectId, "%" + key + "%", pageable
                ) : storyRepo.findStoriesByProjectIdIsAndUserStoryContainingAndCategoryLike(
                projectId, "%" + key + "%", category, pageable
        );

        List<StoryDto> storyDtoList = new ArrayList<>();
        if (page.hasContent()) {
            List<Story> stories = page.getContent();
            storyDtoList = convertToDto(stories);
        }

        return storyDtoList;
    }

    @Override
    public void moveStoriesBackToBacklog(Long sprintId) {
        Sprint sprint = getBySprintId(sprintId);
        List<Story> stories = storyRepo.findAllBySprintId(sprintId);
        Story parent = storyRepo.findStoryByChildStoryIsNullAndStatusLikeAndProjectIdIs(
                "backlog",
                sprint.getProject().getId()
        );

        for (Story story : stories) {
            story.setStatus("backlog");
            if (parent != null) {
                parent.setChildStory(story);
            }
            story.setParentStory(parent);
            story.setChildStory(null);
            story.setSprint(null);
            parent = story;
        }
        storyRepo.saveAll(stories);
    }

    @Override
    public void updateStoriesWhenSprintComplete(Long sprintId) {
        Sprint sprint = getBySprintId(sprintId);
        List<Story> stories = storyRepo.findAllBySprintId(sprintId);
        Story parent = storyRepo.findStoryByChildStoryIsNullAndStatusLikeAndProjectIdIs(
                "backlog",
                sprint.getProject().getId()
        );
        for (Story story : stories) {
            if (story.getStatus().equals("completed")) {
                continue;
            }
            if (story.getStatus().equals("done")) {
                story.setStatus("completed");
                story.setParentStory(null);
                story.setChildStory(null);
            } else {
                story.setStatus("backlog");
                if (parent != null) {
                    parent.setChildStory(story);
                }
                story.setParentStory(parent);
                story.setChildStory(null);
                story.setSprint(null);
                parent = story;
            }
        }
        storyRepo.saveAll(stories);
    }
}

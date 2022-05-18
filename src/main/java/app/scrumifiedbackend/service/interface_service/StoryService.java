package app.scrumifiedbackend.service.interface_service;

import app.scrumifiedbackend.dto.PaginationDto;
import app.scrumifiedbackend.dto.StoryDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface StoryService extends Service<StoryDto> {

    List<StoryDto> findAllStoriesBelongToSprint (Long id);

    PaginationDto<List<StoryDto>> findStoriesBelongToUserAndProject(Long id, String key, Pageable pageable, Long projectId, String exceptStatus);

    StoryDto dragUpdate(Long id, StoryDto storyDto, Boolean isTopDown);

    List<StoryDto> findStoriesBelongToProjectWithBacklog(Long id, String key, String category, Pageable pageable);

    List<StoryDto> findStoriesBelongToProject(Long id, String key, String category, Pageable pageable);

    void moveStoriesBackToBacklog(Long sprintId);

    void updateStoriesWhenSprintComplete(Long sprintId);
}

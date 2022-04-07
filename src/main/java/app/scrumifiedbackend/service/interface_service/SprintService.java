package app.scrumifiedbackend.service.interface_service;

import app.scrumifiedbackend.dto.SprintDto;
import app.scrumifiedbackend.dto.StoryDto;
import app.scrumifiedbackend.entity.Story;

import java.util.List;

public interface SprintService extends Service<SprintDto> {
    List<SprintDto> findAllSprintBelongToProject(Long id);

    SprintDto appendStoryIntoSprint(Long storyId, Long sprintId);

    SprintDto removeStoryOutOfSprint(Story story, Long sprintId);

}

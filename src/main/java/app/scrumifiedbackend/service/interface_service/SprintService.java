package app.scrumifiedbackend.service.interface_service;

import app.scrumifiedbackend.dto.SprintDto;
import app.scrumifiedbackend.dto.StoryDto;

import java.util.List;

public interface SprintService extends Service<SprintDto> {
    List<SprintDto> findAllSprintBelongToProject(Long id);
}

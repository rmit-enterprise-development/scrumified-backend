package app.scrumifiedbackend.service.interface_service;

import app.scrumifiedbackend.dto.StoryDto;

import java.util.List;

public interface StoryService extends Service<StoryDto> {
    List<StoryDto> findAllStoriesBelongToProject (Long id);
}

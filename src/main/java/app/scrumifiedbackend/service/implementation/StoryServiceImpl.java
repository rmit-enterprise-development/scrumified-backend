package app.scrumifiedbackend.service.implementation;

import app.scrumifiedbackend.dto.StoryDto;
import app.scrumifiedbackend.entity.Story;
import app.scrumifiedbackend.service.interface_service.StoryService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StoryServiceImpl implements StoryService {

    @Override
    public List<StoryDto> findAll() {
        return null;
    }

    @Override
    public StoryDto findOne(Long id) {
        return null;
    }

    @Override
    public StoryDto create(StoryDto input) {
        return null;
    }

    @Override
    public StoryDto update(Long id, StoryDto input) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }
}

package app.scrumifiedbackend.service.implementation;

import app.scrumifiedbackend.dto.SprintDto;
import app.scrumifiedbackend.entity.Sprint;
import app.scrumifiedbackend.service.interface_service.SprintService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SprintServiceImpl implements SprintService {

    @Override
    public List<SprintDto> findAll() {
        return null;
    }

    @Override
    public SprintDto findOne(Long id) {
        return null;
    }

    @Override
    public SprintDto create(SprintDto input) {
        return null;
    }

    @Override
    public SprintDto update(Long id, SprintDto input) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }
}

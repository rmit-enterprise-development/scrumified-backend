package app.scrumifiedbackend.service.implementation;

import app.scrumifiedbackend.dto.SprintDto;
import app.scrumifiedbackend.entity.Project;
import app.scrumifiedbackend.entity.Sprint;
import app.scrumifiedbackend.exception.EntityNotFoundException;
import app.scrumifiedbackend.repository.ProjectRepo;
import app.scrumifiedbackend.repository.SprintRepo;
import app.scrumifiedbackend.service.interface_service.SprintService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class SprintServiceImpl implements SprintService {
    private ModelMapper modelMapper;
    private ProjectRepo projectRepo;
    private SprintRepo sprintRepo;

    @Override
    public List<SprintDto> findAll() {
        List<Sprint> sprints = getAll();
        return convertToDto(sprints);
    }

    private List<SprintDto> convertToDto(List<Sprint> sprints) {
        List<SprintDto> sprintDtoList = new ArrayList<>();
        for (Sprint sprint : sprints) {
            sprintDtoList.add(modelMapper.map(sprint, SprintDto.class));
        }
        return sprintDtoList;
    }

    private List<Sprint> getAll() {
        return sprintRepo.findAll();
    }

    @Override
    public SprintDto findOne(Long id) {
        Sprint sprint = getBySprintId(id);
        return returnFromDto(sprint);
    }

    private SprintDto returnFromDto(Sprint sprint) {
        SprintDto sprintDto = modelMapper.map(sprint, SprintDto.class);
        return sprintDto;
    }

    private Sprint getBySprintId(Long id) {
        return sprintRepo.findById(id).orElseThrow(() -> new EntityNotFoundException("ID " + id + " not exist"));
    }

    @Override
    public SprintDto create(SprintDto input) {
        Sprint sprint = modelMapper.map(input, Sprint.class);
        Project project = projectRepo.getById(input.getProjectId());
        sprint.setProject(project);
        sprint = sprintRepo.save(sprint);
        //Todo: generate relationship with the stories
        input.setId(sprint.getId());
        return input;
    }

    @Override
    public SprintDto update(Long id, SprintDto input) {
        return null;
    }

    @Override
    public void delete(Long id) {
        Sprint sprint = getBySprintId(id);
        sprintRepo.delete(sprint);
    }

    @Override
    public List<SprintDto> findAllSprintBelongToProject(Long id) {
        List<SprintDto> sprintDtoList = new ArrayList<>();
        List<Sprint> sprintList = sprintRepo.findAllBelongTo(id);
        for (Sprint sprint : sprintList) {
            sprintDtoList.add(modelMapper.map(sprint, SprintDto.class));
        }
        return sprintDtoList;
    }
}

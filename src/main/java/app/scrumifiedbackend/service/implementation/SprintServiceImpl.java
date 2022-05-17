package app.scrumifiedbackend.service.implementation;

import app.scrumifiedbackend.dto.PaginationDto;
import app.scrumifiedbackend.dto.SprintDto;
import app.scrumifiedbackend.entity.Project;
import app.scrumifiedbackend.entity.Sprint;
import app.scrumifiedbackend.entity.SprintStat;
import app.scrumifiedbackend.exception.EntityNotFoundException;
import app.scrumifiedbackend.exception.EntityNotSaveException;
import app.scrumifiedbackend.mapping.ModelMapperSingleton;
import app.scrumifiedbackend.repository.ProjectRepo;
import app.scrumifiedbackend.repository.SprintRepo;
import app.scrumifiedbackend.service.interface_service.SprintService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class SprintServiceImpl implements SprintService {
    private ProjectRepo projectRepo;
    private SprintRepo sprintRepo;

    private List<SprintDto> convertToDto(List<Sprint> sprints) {
        List<SprintDto> sprintDtoList = new ArrayList<>();
        for (Sprint sprint : sprints) {
            sprintDtoList.add(ModelMapperSingleton.modelMapper.map(sprint, SprintDto.class));
        }
        return sprintDtoList;
    }

    private Sprint save(Sprint sprint) {
        try {
            return sprintRepo.save(sprint);
        } catch (RuntimeException e) {
            throw new EntityNotSaveException(e.getCause().getCause().getMessage());
        }
    }

    @Override
    public PaginationDto<List<SprintDto>> findAll(String key, Pageable pageable) {
        Page<Sprint> page = sprintRepo.findByKeyLike("%" + key + "%", pageable);

        List<SprintDto> sprintDtoList = new ArrayList<>();
        if (page.hasContent()) {
            List<Sprint> sprints = page.getContent();
            sprintDtoList = convertToDto(sprints);
        }
        return new PaginationDto<>(sprintDtoList, page.getTotalElements(), page.getTotalPages(), page.getNumber());
    }

    @Override
    public SprintDto findOne(Long id) {
        Sprint sprint = getBySprintId(id);
        return ModelMapperSingleton.modelMapper.map(sprint, SprintDto.class);
    }

    private Sprint getBySprintId(Long id) {
        return sprintRepo.findById(id).orElseThrow(() -> new EntityNotFoundException("ID " + id + " not exist"));
    }

    @Override
    public SprintDto create(SprintDto input) {
        Sprint sprint = ModelMapperSingleton.modelMapper.map(input, Sprint.class);
        Project project = projectRepo.getById(input.getProjectId());
        sprint.setProject(project);
        sprint = sprintRepo.save(sprint);
        input.setId(sprint.getId());
        return input;
    }

    @Override
    public SprintDto update(Long id, SprintDto input) {
        Sprint sprint = getBySprintId(id);

        if (input.getGoal() != null && !input.getGoal().equals(sprint.getGoal())) {
            sprint.setGoal(input.getGoal());
        }

        if (input.getDefOfDone() != null && !input.getDefOfDone().equals(sprint.getDefOfDone())) {
            sprint.setDefOfDone(input.getDefOfDone());
        }

        if (input.getStatus() != null && !input.getStatus().equals(sprint.getStatus())) {
            sprint.setStatus(input.getStatus());
        }

        if (input.getStartDate() != null && !input.getStartDate().equals(sprint.getStartDate())) {
            sprint.setStartDate(input.getStartDate());
        }

        if (input.getEndDate() != null && !input.getEndDate().equals(sprint.getEndDate())) {
            sprint.setEndDate(input.getEndDate());
        }
        sprint = save(sprint);
        return ModelMapperSingleton.modelMapper.map(sprint, SprintDto.class);
    }

    @Override
    public void delete(Long id) {
        Sprint sprint = getBySprintId(id);
        sprintRepo.delete(sprint);
    }

    @Override
    public List<SprintDto> findAllSprintBelongToProject(Long id, boolean includePercentage) {
        List<SprintDto> sprintDtoList = new ArrayList<>();
        if (includePercentage) {
            List<SprintStat> sprintStats = sprintRepo.getALlSprintsAndPercentage(id);
            for (SprintStat sprintStat : sprintStats) {
                sprintDtoList.add(ModelMapperSingleton.modelMapper.map(sprintStat, SprintDto.class));
            }
        } else {
            List<Sprint> sprintList = sprintRepo.findAllBelongTo(id);
            for (Sprint sprint : sprintList) {
                sprintDtoList.add(ModelMapperSingleton.modelMapper.map(sprint, SprintDto.class));
            }
        }
        return sprintDtoList;
    }

    @Override
    public SprintDto getCurrentSprint(Long projectId) {
        Sprint sprint = sprintRepo.findByProjectIdAndStatusNotLike(projectId, "%done%");
        return sprint == null ? null : ModelMapperSingleton.modelMapper.map(sprint, SprintDto.class);
    }
}

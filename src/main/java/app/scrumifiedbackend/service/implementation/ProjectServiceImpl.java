package app.scrumifiedbackend.service.implementation;

import app.scrumifiedbackend.dto.PaginationDto;
import app.scrumifiedbackend.dto.ProjectDto;
import app.scrumifiedbackend.entity.*;
import app.scrumifiedbackend.exception.EntityNotFoundException;
import app.scrumifiedbackend.exception.EntityNotSaveException;
import app.scrumifiedbackend.mapping.ModelMapperSingleton;
import app.scrumifiedbackend.repository.*;
import app.scrumifiedbackend.service.interface_service.ProjectService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class ProjectServiceImpl implements ProjectService {
    private ProjectRepo projectRepo;
    private UserRepo userRepo;
    private UserProjectRepo userProjectRepo;
    private SprintRepo sprintRepo;
    private StoryRepo storyRepo;

    private List<ProjectDto> convertToDto(List<Project> projects) {
        List<ProjectDto> projectDtoList = new ArrayList<>();
        for (Project project : projects) {
            projectDtoList.add(ModelMapperSingleton.modelMapper.map(project, ProjectDto.class));
        }
        return projectDtoList;
    }

    private Project getById(Long id) {
        return projectRepo.findById(id).orElseThrow(() -> new EntityNotFoundException("Project ID " + id + " not exist"));
    }

    private Project save(Project project) {
        try {
            return projectRepo.save(project);
        } catch (RuntimeException e) {
            throw new EntityNotSaveException(e.getCause().getCause().getMessage());
        }
    }

    private PaginationDto<List<ProjectDto>> getListPaginationDto(Page<Project> page) {
        List<ProjectDto> projectDtoList = new ArrayList<>();
        if (page.hasContent()) {
            List<Project> projects = page.getContent();
            projectDtoList = convertToDto(projects);
        }
        return new PaginationDto<>(projectDtoList, page.getTotalElements(), page.getTotalPages(), page.getNumber());
    }


    private void saveUserProject(ProjectDto input, Project project) {
        for (Long participantId : input.getParticipantsId()) {
            User participant = userRepo.getById(participantId);
            UserProjectKey key = new UserProjectKey(participantId, project.getId());
            UserProject userProject = new UserProject(key, participant, project);
            project.addUserProject(userProject);
            userProjectRepo.save(userProject);
        }
    }

    @Override
    public ProjectDto findProjectWithPoints(Long projectId) {
        Project project = getById(projectId);
        Long totalPoints = projectRepo.getAllPoints(projectId);
        Long todoPoints = projectRepo.getAllPointsByStatus(projectId, "todo");
        Long inProcessPoints = projectRepo.getAllPointsByStatus(projectId, "in process");
        Long donePoints = projectRepo.getAllPointsByStatus(projectId, "done");
        ProjectDto projectDto = ModelMapperSingleton.modelMapper.map(project, ProjectDto.class);
        projectDto.setTotalPoints(totalPoints);
        projectDto.setTodoPoints(todoPoints);
        projectDto.setInProgressPoints(inProcessPoints);
        projectDto.setDonePoints(donePoints);
        return projectDto;
    }

    @Override
    public PaginationDto<List<ProjectDto>> findProjectBelongToUser(Long id, String key, Pageable pageable) {
        Page<Project> page = projectRepo.findProjectsByOwnerIdOrParticipatedIdAndTitleContaining(id, "%" + key + "%", pageable);
        return getListPaginationDto(page);
    }

    @Override
    public PaginationDto<List<ProjectDto>> findAll(String key, Pageable pageable) {
        Page<Project> page = projectRepo.findByKeyLike("%" + key + "%", pageable);
        return getListPaginationDto(page);
    }

    @Override
    public ProjectDto findOne(Long id) {
        Project project = getById(id);
        return ModelMapperSingleton.modelMapper.map(project, ProjectDto.class);
    }

    @Override
    public ProjectDto create(ProjectDto input) {
        Project project = ModelMapperSingleton.modelMapper.map(input, Project.class);
        User owner = userRepo.getById(input.getOwnerId());
        project.setOwner(owner);
        project.setCreatedDate(Instant.now().getEpochSecond());
        project = projectRepo.save(project);
        if (input.getParticipantsId() != null) {
            saveUserProject(input, project);
        }
        return ModelMapperSingleton.modelMapper.map(project, ProjectDto.class);
    }

    @Override
    public ProjectDto update(Long id, ProjectDto input) {
        Project project = getById(id);

        if (input.getTitle() != null && !input.getTitle().equals(project.getTitle())) {
            project.setTitle(input.getTitle());
        }
        List<UserProject> userProjectList = userProjectRepo.findAllByProjectIdIs(id);

        if (input.getParticipantsId() != null) {
            for (UserProject userProject : userProjectList) {
                if (!input.getParticipantsId().contains(userProject.getUser().getId())) {
                    List<Story> stories = storyRepo
                            .findAllByAssigneeIdIsAndProjectIdIs(userProject.getUser().getId(), project.getId());
                    for (Story story : stories) {
                        story.setAssignee(project.getOwner());
                    }
                    storyRepo.saveAll(stories);

                    input.getParticipantsId().remove(userProject.getUser().getId());
                    userProjectRepo.delete(userProject);
                }
            }
            saveUserProject(input, project);
        } else {
            userProjectRepo.deleteAll(userProjectList);
        }

        project = save(project);
        return ModelMapperSingleton.modelMapper.map(project, ProjectDto.class);
    }

    @Override
    public void delete(Long id) {
        Project project = getById(id);
        List<Sprint> sprints = sprintRepo.getAllByProjectId(id);
        List<Story> stories = storyRepo.findAllByProjectId(id);
        storyRepo.deleteAll(stories);
        sprintRepo.deleteAll(sprints);
        projectRepo.delete(project);
    }
}

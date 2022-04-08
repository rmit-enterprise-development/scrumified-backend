package app.scrumifiedbackend.service.implementation;

import app.scrumifiedbackend.dto.ProjectDto;
import app.scrumifiedbackend.entity.Project;
import app.scrumifiedbackend.entity.User;
import app.scrumifiedbackend.entity.UserProject;
import app.scrumifiedbackend.entity.UserProjectKey;
import app.scrumifiedbackend.exception.EntityNotFoundException;
import app.scrumifiedbackend.exception.EntityNotSaveException;
import app.scrumifiedbackend.repository.ProjectRepo;
import app.scrumifiedbackend.repository.UserProjectRepo;
import app.scrumifiedbackend.repository.UserRepo;
import app.scrumifiedbackend.service.interface_service.ProjectService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ProjectServiceImpl implements ProjectService {
    private ProjectRepo projectRepo;
    private UserRepo userRepo;
    private UserProjectRepo userProjectRepo;
    private ModelMapper modelMapper;


    @Override
    public List<ProjectDto> findAll() {
        return null;
    }

    @Override
    public ProjectDto findOne(Long id) {
        Project project = getById(id);
        System.out.println(project);
        return modelMapper.map(project, ProjectDto.class);
    }

    @Override
    public ProjectDto create(ProjectDto input) {
        Project project = modelMapper.map(input, Project.class);
        User owner = userRepo.getById(input.getOwnerId());
        project.setOwner(owner);
        project = projectRepo.save(project);
        for (Long participantId : input.getParticipantsId()) {
            User participant = userRepo.getById(participantId);
            UserProjectKey key = new UserProjectKey(participantId, project.getId());
            UserProject userProject = new UserProject(key, participant, project);
            project.addUserProject(userProject);
            userProjectRepo.save(userProject);
        }
        input.setId(project.getId());
        return input;
    }

    @Override
    public ProjectDto update(Long id, ProjectDto input) {
        Project project = getById(id);

        if (input.getTitle() != null && !input.getTitle().equals(project.getTitle())) {
            project.setTitle(input.getTitle());
        }

        project = save(project);
        return modelMapper.map(project, ProjectDto.class);
    }

    @Override
    public void delete(Long id) {
        Project project = getById(id);
        projectRepo.delete(project);
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

    @Override
    public Long pointsOfProject(Long projectId) {
        return projectRepo.getAllPoints(projectId);
    }

    @Override
    public Long pointsOfProjectByStatus(Long projectId, String status) {
        return projectRepo.getAllPointsByStatus(projectId, status);
    }

    @Override
    public Long pointsOfUser(Long projectId, Long userId) {
        return projectRepo.getAllPointsForUser(projectId, userId);
    }

    @Override
    public Long pointsOfUserByStatus(Long projectId, Long userId, String status) {
        return projectRepo.getAllPointsForUserByStatus(projectId, userId, status);
    }
}

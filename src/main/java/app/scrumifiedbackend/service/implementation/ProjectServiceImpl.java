package app.scrumifiedbackend.service.implementation;

import app.scrumifiedbackend.dto.ProjectDto;
import app.scrumifiedbackend.entity.Project;
import app.scrumifiedbackend.entity.User;
import app.scrumifiedbackend.entity.UserProject;
import app.scrumifiedbackend.entity.UserProjectKey;
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
        return null;
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
        return null;
    }

    @Override
    public void delete(Long id) {

    }


}

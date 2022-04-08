package app.scrumifiedbackend.dto;

import app.scrumifiedbackend.entity.Project;
import app.scrumifiedbackend.entity.User;
import app.scrumifiedbackend.entity.UserProject;
import app.scrumifiedbackend.repository.ProjectRepo;
import app.scrumifiedbackend.repository.UserProjectRepo;
import app.scrumifiedbackend.repository.UserRepo;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Data
@Getter
@Setter
public class ProjectDto {
    private Long id;
    private String title;
    private String createdDate;
    private Long ownerId;
    private List<Long> participantsId;
    private Long totalPoints;
    private Long todoPoints;
    private Long inProgressPoints;
    private Long donePoints;

    public Boolean addParticipantId(Long id) {
        if (participantsId == null) {
            participantsId = new ArrayList<>();
        }
        return participantsId.add(id);
    }

    // bug
//    public List<UserProject> getParticipant() {
//        ProjectRepo projectRepo = null;
//        UserRepo userRepo = null;
//        List<Long> participants = projectRepo.getById(id).getParticipatedId();
//        List<UserProject> participantUsers = new ArrayList<>();
//
//        for (Long UserId : participants) {
//            UserProject userProject = null;
//            User user = userRepo.getById(UserId);
//            Project project = projectRepo.getById(id);
//
//            userProject.setUser(user);
//            userProject.setProject(project);
//
//            participantUsers.add(userProject);
//        }
//
//        return participantUsers;
//    }

}

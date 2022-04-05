package app.scrumifiedbackend.dto;

import app.scrumifiedbackend.entity.User;
import app.scrumifiedbackend.entity.UserProject;
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

    public Boolean addParticipantId(Long id) {
        if (participantsId == null) {
            participantsId = new ArrayList<>();
        }
        return participantsId.add(id);
    }
}

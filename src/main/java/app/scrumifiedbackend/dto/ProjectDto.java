package app.scrumifiedbackend.dto;

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
//    private List<Story> createdStories;

    public Boolean addParticipantId(Long id) {
        if (participantsId == null) {
            participantsId = new ArrayList<>();
        }
        return participantsId.add(id);
    }

//    public Boolean addStory(Story story) {
//        if (createdStories.isEmpty()) {
//            createdStories = new ArrayList<>();
//        }
//        return createdStories.add(story);
//    }
}

package app.scrumifiedbackend.dto;

import app.scrumifiedbackend.mapping.ModelMapperSingleton;
import app.scrumifiedbackend.entity.User;
import com.fasterxml.jackson.annotation.JsonFilter;
import lombok.*;

import java.util.*;

@Data
@JsonFilter(value = "projectFilter")
public class ProjectDto {
    private Long id;
    private String title;
    private Long createdDate;
    private Long ownerId;
    private List<Long> participantsId;

    private UserDto owner;
    private List<UserDto> participants;

    private Long totalPoints;
    private Long todoPoints;
    private Long inProgressPoints;
    private Long donePoints;

    public void toOwnerDto(User user) {
        setOwner(ModelMapperSingleton.modelMapper.map(user, UserDto.class));
    }

    public void toParticipantsDto(List<User> users) {
        for (User user : users) {
            addParticipants(ModelMapperSingleton.modelMapper.map(user, UserDto.class));
        }
    }

    public void addParticipants(UserDto userDto) {
        if (participants == null) {
            participants = new ArrayList<>();
        }
        participants.add(userDto);
    }

    private static final List<String> params = Arrays.asList(
            "id", "title", "createdDate",
            "ownerId", "participantsId",
            "owner", "participants",
            "totalPoints", "todoPoints",
            "inProgressPoints", "donePoints"
    );

    public static Set<String> getFilter(String... exceptElement) {
        Set<String> result = new HashSet<>(params);
        Arrays.asList(exceptElement).forEach(result::remove);
        return result;
    }

}

package app.scrumifiedbackend.dto;

import app.scrumifiedbackend.entity.Project;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Data
@Getter
@Setter
public class UserDto {
    private Boolean isSuccess;
    private List<String> errorTarget;

    public boolean addErrorTarget(String s) {
        if (errorTarget == null) {
            errorTarget = new ArrayList<>();
        }
        return errorTarget.add(s);
    }

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;

    private List<Project> ownedProjects;
    private List<Project> participatedProjects;
}

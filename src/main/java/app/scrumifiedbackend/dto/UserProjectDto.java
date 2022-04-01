package app.scrumifiedbackend.dto;

import app.scrumifiedbackend.entity.Project;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
@Getter
@Setter
public class UserProjectDto {
    private List<ProjectDto> ownedProjects;
    private List<ProjectDto> participatedProjects;
}

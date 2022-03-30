package app.scrumifiedbackend.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "user_project")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserProject {
    @EmbeddedId
    private UserProjectKey id;

    @ManyToOne()
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    @JsonBackReference(value = "user-project")
    private User user;

    @ManyToOne()
    @MapsId("projectId")
    @JoinColumn(name = "project_id")
    @JsonBackReference(value = "project-user")
    private Project project;
}

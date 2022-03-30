package app.scrumifiedbackend.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password")
    private String password;

    @OneToMany(mappedBy = "user")
    @JsonManagedReference(value = "user-project")
    private List<UserProject> participatedProjects;


    @OneToMany(mappedBy = "id", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Project> ownedProjects;

    public List<Project> getParticipatedProjects() {
        if (participatedProjects == null) {
            return null;
        }
        List<Project> projects = new ArrayList<>();
        for (UserProject participatedProject : participatedProjects) {
            if (Objects.equals(participatedProject.getId().getUserId(), this.id)) {
                projects.add(participatedProject.getProject());
            }
        }
        return projects;
    }
}

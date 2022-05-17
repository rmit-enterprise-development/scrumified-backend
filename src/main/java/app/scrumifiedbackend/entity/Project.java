package app.scrumifiedbackend.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "projects")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "created_date")
    private Long createdDate;

    @OneToMany(mappedBy = "project", cascade = CascadeType.REMOVE)
    @JsonManagedReference(value = "project-user")
    private List<UserProject> participatedUsers;

    @ManyToOne()
    @JoinColumn(name = "owner")
    @JsonBackReference
    private User owner;

    public void addUserProject(UserProject userProject) {
        if (participatedUsers == null) {
            participatedUsers = new ArrayList<>();
        }
        participatedUsers.add(userProject);
    }

    public List<Long> getParticipatedId() {
        if (participatedUsers == null || participatedUsers.size() == 0) {
            return null;
        }
        List<Long> participatedId = new ArrayList<>();
        for (UserProject participatedUser : participatedUsers) {
            Long id = participatedUser.getUser().getId();
            participatedId.add(id);
        }
        return participatedId;
    }

    public List<User> getParticipant() {
        if (participatedUsers == null || participatedUsers.size() == 0) {
            return null;
        }
        List<User> participates = new ArrayList<>();
        for (UserProject participatedUser : participatedUsers) {
            User user = participatedUser.getUser();
            participates.add(user);
        }
        return participates;
    }
}

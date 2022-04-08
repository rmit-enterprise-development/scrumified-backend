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
@Table(name = "sprints")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Sprint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "goal", nullable = false)
    private String goal;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "def_of_done")
    private String defOfDone;

    @Column(name = "start_date")
    private String startDate;

    @Column(name = "end_date")
    private String endDate;

    @ManyToOne()
    @JoinColumn(name = "project")
    @JsonBackReference(value = "project-sprint")
    private Project project;

    @OneToMany(mappedBy = "sprint", cascade = CascadeType.PERSIST)
    @JsonManagedReference(value = "sprint-story")
    private List<Story> storyList;

    public Boolean appendStory(Story story) {
        if (storyList == null) {
            storyList = new ArrayList<>();
        }
        return storyList.add(story);
    }

    public Boolean removeStory(Story story) {
        if (storyList == null) {
            return false;
        }
        return storyList.remove(story);
    }
}

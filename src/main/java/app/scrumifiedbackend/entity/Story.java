package app.scrumifiedbackend.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "stories")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Story {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "points", nullable = false)
    private Integer point;

    @Column(name = "user_story", nullable = false)
    private String userStory;

    @Column(name = "category")
    private String category;

    @Column(name = "created_date")
    private Long createdDate;

    @Column(name = "def_of_done")
    private String defOfDone;

    @Column(name = "status", nullable = false)
    private String status;

    @OneToOne
    @JoinColumn(name = "parent_story")
    private Story parentStory;

    @OneToOne
    @JoinColumn(name = "child_story")
    private Story childStory;

    @ManyToOne()
    @JoinColumn(name = "project")
    @JsonBackReference
    private Project project;

    @ManyToOne
    @JoinColumn(name = "assignee")
    @JsonBackReference
    private User assignee;

    @ManyToOne
    @JoinColumn(name = "sprint")
    @JsonBackReference(value = "sprint-story")
    private Sprint sprint;
}

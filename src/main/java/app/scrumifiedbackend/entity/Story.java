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

    @Column(name = "status", nullable = false)
    private String status;

    @ManyToOne()
    @JoinColumn(name = "project")
    @JsonBackReference
    private Project project;
}

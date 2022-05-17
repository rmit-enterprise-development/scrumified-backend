package app.scrumifiedbackend.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

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
    private Long startDate;

    @Column(name = "end_date")
    private Long endDate;

    @ManyToOne()
    @JoinColumn(name = "project")
    @JsonBackReference(value = "project-sprint")
    private Project project;
}


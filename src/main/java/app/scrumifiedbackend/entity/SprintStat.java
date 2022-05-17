package app.scrumifiedbackend.entity;

import org.springframework.data.rest.core.config.Projection;

@Projection(
        name = "sprintStat",
        types = {Sprint.class}
)
public interface SprintStat {
    Long getId();

    String getGoal();

    String getStatus();

    String getDefOfDone();

    Long getStartDate();

    Long getEndDate();

    Long getProjectId();

    Long getCompletePercentage();
}

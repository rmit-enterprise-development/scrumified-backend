package app.scrumifiedbackend.repository;

import app.scrumifiedbackend.entity.Sprint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SprintRepo extends JpaRepository<Sprint, Long> {
    @Query("select sprint from Sprint sprint where sprint.project.id = ?1")
    List<Sprint> findAllBelongTo(Long projectId);

    @Query("select sum(story.point) from Story story where story.sprint.id = ?1")
    Long getAllPoints(Long sprintId);

    @Query("select sum(story.point) from Story story where story.sprint.id = ?1 and story.status = ?2")
    Long getAllPointsByStatus(Long sprintId, String status);
}

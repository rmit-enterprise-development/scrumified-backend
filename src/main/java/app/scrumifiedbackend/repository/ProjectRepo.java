package app.scrumifiedbackend.repository;

import app.scrumifiedbackend.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProjectRepo extends JpaRepository<Project, Long> {

    @Query("select sum(story.point) from Story story where story.project.id = ?1")
    Long getAllPoints(Long projectId);

    @Query("select sum(story.point) from Story story where story.project.id = ?1 and story.status = ?2")
    Long getAllPointsByStatus(Long projectId, String status);

    @Query("select sum(story.point) from Story story where story.project.id = ?1 and story.assignee.id = ?2")
    Long getAllPointsForUser(Long projectId, Long userId);

    @Query("select sum(story.point) from Story story where story.project.id = ?1 and story.assignee.id = ?2 and story.status = ?3")
    Long getAllPointsForUserByStatus(Long projectId, Long userId, String status);

    @Query("select story.category from Story story where story.project.id = ?1")
    List<String> getAllCategory(Long projectId);
}

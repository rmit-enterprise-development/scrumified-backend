package app.scrumifiedbackend.repository;

import app.scrumifiedbackend.entity.Story;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StoryRepo extends JpaRepository<Story, Long> {

    @Query("select story from Story story where story.project.id = ?1")
    List<Story> findAllBelongToProject(Long id);

    @Query("select sum(story.point) from Story story where story.project.id = ?1")
    Long getAllPoints(Long projectId);

    @Query("select sum(story.point) from Story story where story.project.id = ?1 and story.status = ?2")
    Long getAllPointsByStatus(Long projectId, String status);
}

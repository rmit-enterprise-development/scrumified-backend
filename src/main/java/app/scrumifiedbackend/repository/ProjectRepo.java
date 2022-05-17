package app.scrumifiedbackend.repository;

import app.scrumifiedbackend.entity.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProjectRepo extends JpaRepository<Project, Long> {

    @Query("select sum(story.point) from Story story where story.project.id = ?1")
    Long getAllPoints(Long projectId);

    @Query("select sum(story.point) from Story story where story.project.id = ?1 and story.status = ?2")
    Long getAllPointsByStatus(Long projectId, String status);

    @Query(value = "select project from Project project where project.title like :key",
    countQuery = "select count(project) from Project project where project.title like :key")
    Page<Project> findByKeyLike(@Param("key") String key, Pageable pageable);

    @Query(
            value = "select * from projects project where project.title ~~* :title and " +
                    "(project.owner = :id " +
                    "or :id in (select pair.user_id from user_project pair where project.id = pair.project_id))",
            countQuery = "select count(*) from projects project where project.title ~~* :title and " +
                    "(project.owner = :id " +
                    "or :id in (select pair.user_id from user_project pair where project.id = pair.project_id))",
            nativeQuery = true
    )
    Page<Project> findProjectsByOwnerIdOrParticipatedIdAndTitleContaining(
            @Param(value = "id") Long id, @Param(value = "title") String key, Pageable pageable);
}

package app.scrumifiedbackend.repository;

import app.scrumifiedbackend.entity.Sprint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SprintRepo extends JpaRepository<Sprint, Long> {
    @Query("select sprint from Sprint sprint where sprint.project.id = ?1")
    List<Sprint> findAllBelongTo(Long projectId);
}

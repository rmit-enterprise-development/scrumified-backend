package app.scrumifiedbackend.repository;

import app.scrumifiedbackend.entity.Sprint;
import app.scrumifiedbackend.entity.SprintStat;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SprintRepo extends JpaRepository<Sprint, Long> {
    @Query("select sprint from Sprint sprint where sprint.project.id = ?1")
    List<Sprint> findAllBelongTo(Long projectId);

    List<Sprint> getAllByProjectId(@Param(value = "projectId") Long projectId);

    Sprint findByProjectIdAndStatusNotLike(
            @Param(value = "projectId") Long projectId,
            @Param(value = "status") String status
    );

    @Query(
            value = "select sprints.id, sprints.goal, sprints.status, sprints.def_of_done as defOfDone, " +
                    "sprints.start_date as startDate, sprints.end_date as endDate, sprints.project as projectId, " +
                    "((select coalesce(SUM(story.points), 0) from stories story " +
                    "where story.sprint = sprints.id and " +
                    "(story.status like '%done%' or story.status like '%completed%')) * 100 / " +
                    "(select SUM(story.points) from stories story where story.sprint = sprints.id)) " +
                    "as completePercentage " +
                    "from sprints where sprints.project = :projectId",
            nativeQuery = true
    )
    List<SprintStat> getALlSprintsAndPercentage(@Param(value = "projectId") Long projectId);


    @Query(
            value = "select * from sprints sprint where sprint.goal ~~* :key",
            countQuery = "select count(*) from sprints sprint where sprint.goal ~~* :key",
            nativeQuery = true
    )
    Page<Sprint> findByKeyLike(@Param(value = "key") String key, Pageable pageable);
}

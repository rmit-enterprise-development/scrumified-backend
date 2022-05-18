package app.scrumifiedbackend.repository;

import app.scrumifiedbackend.entity.Story;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StoryRepo extends JpaRepository<Story, Long> {

    List<Story> findAllByProjectId(Long projectId);

    List<Story> findAllBySprintId(Long sprintId);

    List<Story> findAllBySprintIdOrderByParentStoryId(Long id);

    Long countStoriesByStatusLikeAndProjectIdIs(
            @Param(value = "status") String status,
            @Param(value = "projectId") Long projectId
    );

    Story findStoryByChildStoryIsNullAndStatusLikeAndProjectIdIs(
            @Param(value = "status") String status,
            @Param(value = "projectId") Long projectId
    );

    @Query(
            value = "select * from stories story where story.assignee = :assigneeId " +
                    "and story.user_story ~~* :userStory and story.status not like :exceptStatus",
            countQuery = "select count(*) from stories story where story.assignee = :assigneeId " +
                    "and story.user_story ~~* :userStory and story.status not like :exceptStatus",
            nativeQuery = true
    )
    Page<Story> findStoriesByAssigneeIdAndUserStoryContaining(
            @Param(value = "assigneeId") Long id,
            @Param(value = "userStory") String key,
            @Param(value = "exceptStatus") String exceptStatus,
            Pageable pageable
    );

    @Query(
            value = "select * from stories story where story.assignee = :assigneeId and story.project = :projectId" +
                    " and story.user_story ~~* :userStory and story.status not like :exceptStatus",
            countQuery = "select count(*) from stories story where story.assignee = :assigneeId and story.project = " +
                    ":projectId and story.user_story ~~* :userStory and story.status not like :exceptStatus",
            nativeQuery = true
    )
    Page<Story> findStoriesByAssigneeIdAndProjectIdIsAndUserStoryContaining(
            @Param(value = "assigneeId") Long id,
            @Param(value = "userStory") String key, Pageable pageable,
            @Param(value = "exceptStatus") String exceptStatus,
            @Param(value = "projectId") Long projectId
    );

    @Query(
            value = "select * from stories story where story.user_story ~~* :userStory",
            countQuery = "select count(*) from stories story where story.user_story ~~* :userStory",
            nativeQuery = true
    )
    Page<Story> findStoriesByUserStoryContaining(@Param(value = "userStory") String key, Pageable pageable);

    @Query(
            value = "select * from stories story where story.user_story ~~* :userStory " +
                    "and story.project = :projectId and story.sprint is null",
            countQuery = "select count(*) from stories story where story.user_story ~~* :userStory " +
                    "and story.project = :projectId and story.sprint is null",
            nativeQuery = true
    )
    Page<Story> findStoriesByProjectIdIsAndSprintIdIsNullAndUserStoryContaining(
            @Param(value = "projectId") Long projectId,
            @Param(value = "userStory") String key,
            Pageable pageable
    );

    @Query(
            value = "select * from stories story where story.user_story ~~* :userStory " +
                    "and story.project = :projectId and story.sprint is null and story.category = :category",
            countQuery = "select count(*) from stories story where story.user_story ~~* :userStory " +
                    "and story.project = :projectId and story.sprint is null and story.category = :category",
            nativeQuery = true
    )
    Page<Story> findStoriesByProjectIdIsAndSprintIdIsNullAndUserStoryContainingAndCategoryLike(
            @Param(value = "projectId") Long projectId,
            @Param(value = "userStory") String key,
            @Param(value = "category") String category,
            Pageable pageable
    );

    @Query(
            value = "select * from stories story where story.user_story ~~* :userStory " +
                    "and story.project = :projectId",
            countQuery = "select count(*) from stories story where story.user_story ~~* :userStory " +
                    "and story.project = :projectId",
            nativeQuery = true
    )
    Page<Story> findStoriesByProjectIdIsAndUserStoryContaining(
            @Param(value = "projectId") Long projectId,
            @Param(value = "userStory") String key,
            Pageable pageable
    );

    @Query(
            value = "select * from stories story where story.user_story ~~* :userStory " +
                    "and story.project = :projectId and story.category = :category",
            countQuery = "select count(*) from stories story where story.user_story ~~* :userStory " +
                    "and story.project = :projectId and story.category = :category",
            nativeQuery = true
    )
    Page<Story> findStoriesByProjectIdIsAndUserStoryContainingAndCategoryLike(
            @Param(value = "projectId") Long projectId,
            @Param(value = "userStory") String key,
            @Param(value = "category") String category,
            Pageable pageable
    );

    List<Story> findAllByAssigneeIdIsAndProjectIdIs(
            @Param(value = "assigneeId") Long assigneeId,
            @Param(value = "projectId") Long projectId
    );
}

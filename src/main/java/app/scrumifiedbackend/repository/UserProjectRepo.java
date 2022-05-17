package app.scrumifiedbackend.repository;

import app.scrumifiedbackend.entity.UserProject;
import app.scrumifiedbackend.entity.UserProjectKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserProjectRepo extends JpaRepository<UserProject, UserProjectKey> {
    List<UserProject> findAllByProjectIdIs(@Param(value = "projectId") Long projectId);
}

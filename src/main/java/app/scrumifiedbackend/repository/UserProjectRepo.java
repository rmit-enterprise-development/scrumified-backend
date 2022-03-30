package app.scrumifiedbackend.repository;

import app.scrumifiedbackend.entity.UserProject;
import app.scrumifiedbackend.entity.UserProjectKey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserProjectRepo extends JpaRepository<UserProject, UserProjectKey> {
}

package app.scrumifiedbackend.repository;

import app.scrumifiedbackend.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepo extends JpaRepository<Project, Long> {
}

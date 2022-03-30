package app.scrumifiedbackend.repository;

import app.scrumifiedbackend.entity.Sprint;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SprintRepo extends JpaRepository<Sprint, Long> {
}

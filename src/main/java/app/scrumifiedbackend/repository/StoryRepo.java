package app.scrumifiedbackend.repository;

import app.scrumifiedbackend.entity.Story;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoryRepo extends JpaRepository<Story, Long> {
}

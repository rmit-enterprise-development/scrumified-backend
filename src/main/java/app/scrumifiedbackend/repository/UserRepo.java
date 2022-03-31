package app.scrumifiedbackend.repository;

import app.scrumifiedbackend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepo extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);


    @Query("select user from User user where user.id <> ?1")
    List<User> findAllExceptOne(Long id);
}

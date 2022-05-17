package app.scrumifiedbackend.repository;

import app.scrumifiedbackend.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepo extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    @Query(
            value = "select * from users where users.id <> :id and " +
                    "users.email ~~* :key or " +
                    "users.first_name ~~* :key or " +
                    "users.last_name ~~* :key",
            countQuery = "select count(*) from users where users.id <> :id and " +
                    "users.email ~~* :key or " +
                    "users.first_name ~~* :key or " +
                    "users.last_name ~~* :key",
            nativeQuery = true
    )
    Page<User> findAllExceptOne(
            @Param(value = "id") Long id,
            @Param(value = "key") String key,
            Pageable pageable
    );

    @Query(
            value = "select * from users where " +
                    "users.email ~~* :key or " +
                    "users.first_name ~~* :key or " +
                    "users.last_name ~~* :key",
            countQuery = "select count(*) from users where " +
                    "users.email ~~* :key or " +
                    "users.first_name ~~* :key or " +
                    "users.last_name ~~* :key",
            nativeQuery = true
    )
    Page<User> findAll(@Param(value = "key") String key, Pageable pageable);

}

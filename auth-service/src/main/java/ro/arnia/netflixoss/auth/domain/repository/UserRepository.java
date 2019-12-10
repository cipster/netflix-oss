package ro.arnia.netflixoss.auth.domain.repository;

import org.springframework.stereotype.Repository;
import ro.arnia.netflixoss.auth.domain.model.User;

import java.util.Optional;

@Repository
public interface UserRepository extends BaseRepository<User> {
    Optional<User> findByUsername(String username);
}

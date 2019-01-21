package nl.globalorange.compliancewise.auth.domain.repository;

import nl.globalorange.compliancewise.auth.domain.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, String> {
}

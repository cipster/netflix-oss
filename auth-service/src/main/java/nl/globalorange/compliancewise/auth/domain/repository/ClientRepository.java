package nl.globalorange.compliancewise.auth.domain.repository;

import nl.globalorange.compliancewise.auth.domain.model.Client;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientRepository extends BaseRepository<Client> {

    Optional<Client> findByName(String name);
}

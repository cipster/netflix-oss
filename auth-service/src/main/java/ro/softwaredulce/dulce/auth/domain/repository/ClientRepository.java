package ro.softwaredulce.dulce.auth.domain.repository;

import org.springframework.stereotype.Repository;
import ro.softwaredulce.dulce.auth.domain.model.Client;

import java.util.Optional;

@Repository
public interface ClientRepository extends BaseRepository<Client> {

    Optional<Client> findByName(String name);
}

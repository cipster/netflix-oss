package nl.globalorange.compliancewise.auth.domain.repository;

import nl.globalorange.compliancewise.auth.domain.model.BaseEntity;
import org.springframework.data.repository.CrudRepository;

public interface BaseRepository<ENTITY extends BaseEntity> extends CrudRepository<ENTITY, String> {
}

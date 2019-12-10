package ro.arnia.netflixoss.auth.domain.repository;

import org.springframework.data.repository.CrudRepository;
import ro.arnia.netflixoss.auth.domain.model.BaseEntity;

public interface BaseRepository<ENTITY extends BaseEntity> extends CrudRepository<ENTITY, String> {
}

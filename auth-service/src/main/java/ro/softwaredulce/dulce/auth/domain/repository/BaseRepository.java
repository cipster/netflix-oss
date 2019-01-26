package ro.softwaredulce.dulce.auth.domain.repository;

import org.springframework.data.repository.CrudRepository;
import ro.softwaredulce.dulce.auth.domain.model.BaseEntity;

public interface BaseRepository<ENTITY extends BaseEntity> extends CrudRepository<ENTITY, String> {
}

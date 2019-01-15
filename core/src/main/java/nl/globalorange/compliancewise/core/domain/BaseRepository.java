package nl.globalorange.compliancewise.core.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.Optional;

@NoRepositoryBean
public interface BaseRepository<ENTITY extends BaseEntity> extends PagingAndSortingRepository<ENTITY, String> {


    @RestResource(exported = false)
    @Override
    Iterable<ENTITY> findAll(Sort sort);

    @RestResource(exported = false)
    @Override
    Page<ENTITY> findAll(Pageable pageable);

    @RestResource(exported = false)
    @Override
    <S extends ENTITY> S save(S s);

    @RestResource(exported = false)
    @Override
    <S extends ENTITY> Iterable<S> saveAll(Iterable<S> iterable);

    @RestResource(exported = false)
    @Override
    Optional<ENTITY> findById(String s);

    @RestResource(exported = false)
    @Override
    boolean existsById(String s);

    @RestResource(exported = false)
    @Override
    Iterable<ENTITY> findAll();

    @RestResource(exported = false)
    @Override
    Iterable<ENTITY> findAllById(Iterable<String> iterable);

    @RestResource(exported = false)
    @Override
    long count();

    @RestResource(exported = false)
    @Override
    void deleteById(String s);

    @RestResource(exported = false)
    @Override
    void delete(ENTITY entity);

    @RestResource(exported = false)
    @Override
    void deleteAll(Iterable<? extends ENTITY> iterable);

    @RestResource(exported = false)
    @Override
    void deleteAll();
}

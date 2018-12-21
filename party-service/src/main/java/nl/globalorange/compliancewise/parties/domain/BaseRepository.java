package nl.globalorange.compliancewise.parties.domain;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;
import java.util.Optional;

@NoRepositoryBean
public interface BaseRepository<ENTITY extends BaseEntity> extends MongoRepository<ENTITY, String> {
    @RestResource(exported = false)
    @Override
    <S extends ENTITY> List<S> saveAll(Iterable<S> iterable);

    @RestResource(exported = false)
    @Override
    List<ENTITY> findAll();

    @RestResource(exported = false)
    @Override
    List<ENTITY> findAll(Sort sort);

    @RestResource(exported = false)
    @Override
    <S extends ENTITY> S insert(S s);

    @RestResource(exported = false)
    @Override
    <S extends ENTITY> List<S> insert(Iterable<S> iterable);

    @RestResource(exported = false)
    @Override
    <S extends ENTITY> List<S> findAll(Example<S> example);

    @RestResource(exported = false)
    @Override
    <S extends ENTITY> List<S> findAll(Example<S> example, Sort sort);

    @RestResource(exported = false)
    @Override
    Page<ENTITY> findAll(Pageable pageable);

    @RestResource(exported = false)
    @Override
    <S extends ENTITY> S save(S s);

    @RestResource(exported = false)
    @Override
    Optional<ENTITY> findById(String s);

    @RestResource(exported = false)
    @Override
    boolean existsById(String s);

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

    @RestResource(exported = false)
    @Override
    <S extends ENTITY> Optional<S> findOne(Example<S> example);

    @RestResource(exported = false)
    @Override
    <S extends ENTITY> Page<S> findAll(Example<S> example, Pageable pageable);

    @RestResource(exported = false)
    @Override
    <S extends ENTITY> long count(Example<S> example);

    @RestResource(exported = false)
    @Override
    <S extends ENTITY> boolean exists(Example<S> example);
}

package nl.globalorange.compliancewise.parties.domain.repository;


import nl.globalorange.compliancewise.parties.domain.BaseRepository;
import nl.globalorange.compliancewise.parties.domain.model.Party;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.core.annotation.RestResource;

@RestResource(rel = Party.PATH, path = Party.PATH)
public interface PartyRepository extends BaseRepository<Party> {

    @RestResource
    @Override
    Page<Party> findAll(Pageable pageable);

    @RestResource
    @Override
    <S extends Party> S save(S entity);
}

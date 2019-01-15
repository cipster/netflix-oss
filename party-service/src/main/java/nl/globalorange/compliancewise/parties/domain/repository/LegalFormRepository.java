package nl.globalorange.compliancewise.parties.domain.repository;


import nl.globalorange.compliancewise.core.domain.BaseRepository;
import nl.globalorange.compliancewise.parties.domain.model.LegalForm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.core.annotation.RestResource;

@RestResource(rel = LegalForm.PATH, path = LegalForm.PATH)
public interface LegalFormRepository extends BaseRepository<LegalForm> {

    @RestResource
    @Override
    Page<LegalForm> findAll(Pageable pageable);

    @RestResource
    @Override
    <S extends LegalForm> S save(S entity);
}

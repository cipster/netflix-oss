package nl.globalorange.compliancewise.parties.domain.repository;


import nl.globalorange.compliancewise.parties.domain.BaseRepository;
import nl.globalorange.compliancewise.parties.domain.model.BankAccount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.core.annotation.RestResource;

@RestResource(rel = BankAccount.PATH, path = BankAccount.PATH)
public interface BankAccountRepository extends BaseRepository<BankAccount> {

    @RestResource
    @Override
    Page<BankAccount> findAll(Pageable pageable);

    @RestResource
    @Override
    <S extends BankAccount> S save(S entity);
}

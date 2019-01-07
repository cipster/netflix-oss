package nl.globalorange.compliancewise.parties.domain.model.party;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.neovisionaries.i18n.CountryCode;
import lombok.Data;
import lombok.EqualsAndHashCode;
import nl.globalorange.compliancewise.parties.domain.BaseEntity;
import nl.globalorange.compliancewise.parties.domain.model.BankAccount;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.Set;

import static javax.persistence.EnumType.STRING;

@EqualsAndHashCode(callSuper = true)
@Data

@JsonDeserialize(as = Company.class)
@Entity
@Table(name = "parties")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING)
@GenericGenerator(
        name = BaseEntity.Constants.SEQ_GENERATOR,
        strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
        parameters = {
                @Parameter(name = "sequence_name", value = Party.SEQUENCE_NAME),
                @Parameter(name = "initial_value", value = "1"),
                @Parameter(name = "increment_size", value = "1")
        }
)
public class Party extends BaseEntity {
    public static final String PATH = "parties";
    public static final String SEQUENCE_NAME = "party_id_seq";

    @Enumerated(STRING)
    @Column(insertable = false, updatable = false)
    private Type type;

    private String name;

    private String externalId;

    @Enumerated(STRING)
    private CountryCode countryOfResidence;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id")
    private Address address;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "secondary_address_id")
    private Address secondaryAddress;

    @OneToMany(mappedBy = "party")
    private Set<BankAccount> bankAccounts;

    public enum Type {
        COMPANY,
        INDIVIDUAL
    }
}

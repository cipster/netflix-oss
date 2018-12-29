package nl.globalorange.compliancewise.parties.domain.model.party;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.neovisionaries.i18n.CountryCode;
import lombok.Data;
import lombok.EqualsAndHashCode;
import nl.globalorange.compliancewise.parties.domain.BaseEntity;
import nl.globalorange.compliancewise.parties.domain.model.BankAccount;
import nl.globalorange.compliancewise.parties.domain.model.formly.FormAnswers;
import nl.globalorange.compliancewise.parties.domain.model.formly.FormQuestions;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
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
@SequenceGenerator(name = "pk_sequence", sequenceName = "party_id_seq", allocationSize = 1)
public class Party extends BaseEntity {
    public static final String PATH = "parties";

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

    @Embedded
    @AttributeOverrides({@AttributeOverride(name = "questions", column = @Column(name = "form_questions"))})
    private FormQuestions formQuestions;

    @Embedded
    @AttributeOverrides({@AttributeOverride(name = "answers", column = @Column(name = "form_answers"))})
    private FormAnswers formAnswers;

    public enum Type {
        COMPANY,
        INDIVIDUAL
    }
}

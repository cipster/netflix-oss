package nl.globalorange.compliancewise.parties.domain.model.party;

import com.neovisionaries.i18n.CountryCode;
import lombok.Data;
import lombok.EqualsAndHashCode;
import nl.globalorange.compliancewise.parties.domain.model.LegalForm;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotEmpty;
import java.time.Instant;

import static javax.persistence.EnumType.STRING;

@Data
@EqualsAndHashCode(callSuper = true)

@Entity
@DiscriminatorValue("COMPANY")
public class Company extends Party {

    @NotEmpty
    private String companyName;

    @ManyToOne
    @JoinColumn(name = "legal_form_id")
    private LegalForm legalForm;

    private Instant dateOfIncorporation;

    private String placeOfIncorporation;

    @Enumerated(STRING)
    private CountryCode countryOfIncorporation;

    @NotEmpty
    private String chamberOfCommerceNumber;
}
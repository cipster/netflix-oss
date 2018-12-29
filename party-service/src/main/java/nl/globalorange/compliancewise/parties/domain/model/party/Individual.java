package nl.globalorange.compliancewise.parties.domain.model.party;

import com.neovisionaries.i18n.CountryCode;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotEmpty;
import java.time.Instant;

import static javax.persistence.EnumType.STRING;

@Data
@EqualsAndHashCode(callSuper = true)

@Entity
@DiscriminatorValue("INDIVIDUAL")
public class Individual extends Party {

    @NotEmpty
    private String firstName;
    @NotEmpty
    private String lastName;
    private String passportNumber;

    @Enumerated(STRING)
    private CountryCode nationality;

    @Enumerated(STRING)
    private CountryCode countryOfPassport;

    @Enumerated(STRING)
    private Gender gender;

    private String placeOfBirth;

    private Instant dateOfBirth;

    @Enumerated(STRING)
    private CountryCode countryOfBirth;

    public enum Gender {
        MALE,
        FEMALE,
        UNKNOWN
    }
}
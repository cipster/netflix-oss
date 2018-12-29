package nl.globalorange.compliancewise.parties.domain.model.party;

import com.neovisionaries.i18n.CountryCode;
import lombok.Data;
import lombok.EqualsAndHashCode;
import nl.globalorange.compliancewise.parties.domain.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.SequenceGenerator;

import static javax.persistence.EnumType.STRING;

@Data
@EqualsAndHashCode(callSuper = true)

@Entity
@SequenceGenerator(name = "pk_sequence", sequenceName = "address_id_seq", allocationSize = 1)
public class Address extends BaseEntity {

    private String street;

    private String number;

    private String postCode;

    private String city;

    private String state;

    @Enumerated(STRING)
    private CountryCode country;
}
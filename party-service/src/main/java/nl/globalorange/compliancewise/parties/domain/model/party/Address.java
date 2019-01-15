package nl.globalorange.compliancewise.parties.domain.model.party;

import com.neovisionaries.i18n.CountryCode;
import lombok.Data;
import lombok.EqualsAndHashCode;
import nl.globalorange.compliancewise.core.domain.BaseEntity;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.Entity;
import javax.persistence.Enumerated;

import static javax.persistence.EnumType.STRING;

@Data
@EqualsAndHashCode(callSuper = true)

@Entity
@GenericGenerator(
        name = BaseEntity.Constants.SEQ_GENERATOR,
        strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
        parameters = {
                @Parameter(name = "sequence_name", value = Address.SEQUENCE_NAME),
                @Parameter(name = "initial_value", value = "1"),
                @Parameter(name = "increment_size", value = "1")
        }
)
public class Address extends BaseEntity {

    public static final String SEQUENCE_NAME = "address_id_seq";

    private String street;

    private String number;

    private String postCode;

    private String city;

    private String state;

    @Enumerated(STRING)
    private CountryCode country;
}
package nl.globalorange.compliancewise.parties.domain.model;

import com.neovisionaries.i18n.CountryCode;
import lombok.Data;
import lombok.EqualsAndHashCode;
import nl.globalorange.compliancewise.parties.domain.BaseEntity;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

@Data
@EqualsAndHashCode(callSuper = true)

@Entity
@Table(name = "legal_forms")
@GenericGenerator(
        name = BaseEntity.Constants.SEQ_GENERATOR,
        strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
        parameters = {
                @Parameter(name = "sequence_name", value = LegalForm.SEQUENCE_NAME),
                @Parameter(name = "initial_value", value = "1"),
                @Parameter(name = "increment_size", value = "1")
        }
)
public class LegalForm extends BaseEntity {

    public static final String PATH = "legal-forms";
    public static final String SEQUENCE_NAME = "legal_forms_id_seq";

    @Column(name = "legal_form")
    private String legalFormCode;

    private CountryCode country;

    private String documentAcronym;

    private String title;

    private String englishTitle;

    private Boolean euCountry;

    @Transient
    public String getPrettyTitle() {
        String acronym = "";
        if (documentAcronym != null) {
            acronym = String.format(" (%s)", documentAcronym);
        }
        return String.format("%s%s", englishTitle, acronym);
    }
}
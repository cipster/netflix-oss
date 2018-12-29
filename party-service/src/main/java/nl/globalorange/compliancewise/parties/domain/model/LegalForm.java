package nl.globalorange.compliancewise.parties.domain.model;

import com.neovisionaries.i18n.CountryCode;
import lombok.Data;
import lombok.EqualsAndHashCode;
import nl.globalorange.compliancewise.parties.domain.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

@Data
@EqualsAndHashCode(callSuper = true)

@Entity
@Table(name = "legal_forms")
@SequenceGenerator(name = "pk_sequence", sequenceName = "legal_forms_id_seq", allocationSize = 1)
public class LegalForm extends BaseEntity {

    public static final String PATH = "legal-forms";

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
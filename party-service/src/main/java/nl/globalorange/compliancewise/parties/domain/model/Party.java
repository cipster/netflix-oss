package nl.globalorange.compliancewise.parties.domain.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import nl.globalorange.compliancewise.parties.domain.BaseEntity;
import org.springframework.data.mongodb.core.mapping.Document;

@EqualsAndHashCode(callSuper = true)
@Data
@Document
public class Party extends BaseEntity {
    public static final String PATH = "parties";
}

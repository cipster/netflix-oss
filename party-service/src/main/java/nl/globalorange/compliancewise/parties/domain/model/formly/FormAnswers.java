package nl.globalorange.compliancewise.parties.domain.model.formly;

import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@TypeDef(name = "jsonMap", typeClass = PgJsonType.class,
        parameters = @Parameter(name = PgJsonType.RETURNED_CLASS_TYPE,
                value = "java.util.Map,java.util.Map,java.lang.String, java.lang.Object"))
@Embeddable
public class FormAnswers implements Serializable {

    @Column
    @Type(type = "jsonMap")
    private HashMap<String, Object> answers;

    public Map<String, Object> getAnswers() {
        return answers;
    }

    public void setAnswers(Map<String, Object> answers) {
        this.answers = answers != null ? new HashMap<>(answers) : null;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getAnswers());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FormAnswers)) return false;
        FormAnswers that = (FormAnswers) o;
        return Objects.equals(getAnswers(), that.getAnswers());
    }
}

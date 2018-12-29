package nl.globalorange.compliancewise.parties.domain.model.formly;

import com.google.common.collect.Lists;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@TypeDefs({@TypeDef(name = "jsonListData", typeClass = PgJsonType.class,
        parameters = @Parameter(name = PgJsonType.RETURNED_CLASS_TYPE,
                value = "java.util.List, java.util.List, nl.globalorange.compliancewise.parties.domain.model.formly.FormField"))
})
@Embeddable
public class FormQuestions implements Serializable {
    /**
     * This field defines only the structure of the form. Values appear in {@link FormAnswers}
     */
    @Column
    @Type(type = "jsonListData")
    private List<FormField> questions;

    public List<FormField> getQuestions() {
        return questions;
    }

    public void setQuestions(List<FormField> questions) {
        this.questions = questions != null ? Lists.newArrayList(questions) : null;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getQuestions());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FormQuestions)) return false;
        FormQuestions that = (FormQuestions) o;
        return Objects.equals(getQuestions(), that.getQuestions());
    }
}

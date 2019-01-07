package nl.globalorange.compliancewise.parties.domain;

import lombok.Data;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;
import java.io.Serializable;
import java.time.Instant;

@Data
@MappedSuperclass
@Where(clause = "deleted = 'FALSE'")
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = Constants.SEQ_GENERATOR)
    private Long id;

    @Version
    private Long version;

    private boolean deleted;

    @CreatedDate
    private Instant createdDate;

    @LastModifiedDate
    private Instant lastModifiedDate;

    public static class Constants {
        public static final String SEQ_GENERATOR = "pk_sequence";

        public static final String MAIN_SCHEMA = "main";

        private Constants() {
            // intentionally blank
        }
    }
}
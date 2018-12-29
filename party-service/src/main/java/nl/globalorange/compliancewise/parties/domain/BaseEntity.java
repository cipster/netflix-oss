package nl.globalorange.compliancewise.parties.domain;

import lombok.Data;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;
import java.io.Serializable;
import java.time.Instant;

@Data
@MappedSuperclass
@Where(clause = "deleted = 'FALSE'")
public abstract class BaseEntity implements Serializable {

    @Id
    private String id;

    @Version
    private Long version;

    private boolean deleted;

    @CreatedDate
    private Instant createdDate;

    @LastModifiedDate
    private Instant lastModifiedDate;
}
package ro.softwaredulce.dulce.auth.domain.model;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;

import java.io.Serializable;
import java.time.Instant;

@Data
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
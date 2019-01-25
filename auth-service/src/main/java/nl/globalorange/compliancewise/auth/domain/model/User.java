package nl.globalorange.compliancewise.auth.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mongodb.DBObject;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Instant;
import java.util.List;
import java.util.Set;

import static java.util.Objects.isNull;
import static java.util.stream.Collectors.toList;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@Document(collection = "users")
public class User extends BaseEntity implements UserDetails {

    private String username;

    private String password;

    private String firstName;
    private String lastName;

    private boolean enabled = true;
    private boolean locked = false;

    private Instant expirationDate;
    private Instant credentialExpirationDate;
    private Set<Authorities> authorities;

    public User(DBObject dbObject) {
        this.setId((String) dbObject.get("_id"));
        this.username = (String) dbObject.get("emailAddress");
        this.firstName = (String) dbObject.get("firstName");
        this.lastName = (String) dbObject.get("lastName");
        this.password = (String) dbObject.get("hashedPassword");
        this.enabled = (Boolean) dbObject.get("enabled");
        this.locked = (Boolean) dbObject.get("locked");
        this.expirationDate = (Instant) dbObject.get("accountExpiryDate");
        this.credentialExpirationDate = (Instant) dbObject.get("credentialsExpiryDate");
        this.authorities = (Set<Authorities>) dbObject.get("authorities");
    }

    @Override
    public List<GrantedAuthority> getAuthorities() {
        return authorities.stream()
                .map(authority -> new SimpleGrantedAuthority(authority.securityName()))
                .collect(toList());
    }

    @JsonIgnore
    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        if (isNull(expirationDate)) {
            return true;
        }
        return Instant.now().isBefore(expirationDate);
    }

    @Override
    public boolean isAccountNonLocked() {
        return !locked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        if (isNull(credentialExpirationDate)) {
            return true;
        }
        return Instant.now().isBefore(credentialExpirationDate);
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

}

package ro.softwaredulce.dulce.auth.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Instant;
import java.util.List;

import static java.util.Objects.isNull;
import static java.util.stream.Collectors.toList;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@ToString
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
    private List<String> authorities;

    public User(org.bson.Document document) {
        this.setId(document.getString("_id"));
        this.setDeleted(document.getBoolean("deleted"));
        this.username = document.getString("username");
        this.firstName = document.getString("firstName");
        this.lastName = document.getString("lastName");
        this.password = document.getString("password");
        this.enabled = document.getBoolean("enabled");
        this.locked = document.getBoolean("locked");
        this.expirationDate = (Instant) document.get("accountExpiryDate");
        this.credentialExpirationDate = (Instant) document.get("credentialsExpiryDate");
        this.authorities = (List<String>) document.get("authorities");
    }

    @Override
    public List<GrantedAuthority> getAuthorities() {
        return authorities.stream()
                .map(authority -> new SimpleGrantedAuthority(Authority.valueOf(authority).securityName()))
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

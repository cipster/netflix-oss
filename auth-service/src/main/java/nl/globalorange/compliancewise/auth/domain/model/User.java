package nl.globalorange.compliancewise.auth.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Instant;
import java.util.List;

@Data
@Document(collection = "users")
public class User implements UserDetails {

    @Id
    private String id;

    private String username;

    private String password;

    private boolean enabled = true;
    private boolean locked = false;

    private Instant expirationDate;
    private Instant credentialExpirationDate;

    @Override
    public List<GrantedAuthority> getAuthorities() {
        return null;
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
        return Instant.now().isBefore(expirationDate);
    }

    @Override
    public boolean isAccountNonLocked() {
        return !locked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return Instant.now().isBefore(credentialExpirationDate);
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

}

package ro.arnia.netflixoss.administration.domain.model;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class User {
    @NotNull
    @Length(min = 3, max = 40)
    private String username;

    @NotNull
    @Length(min = 6, max = 40)
    private String password;

    @NotEmpty
    private String firstName;

    @NotEmpty
    private String lastName;
}

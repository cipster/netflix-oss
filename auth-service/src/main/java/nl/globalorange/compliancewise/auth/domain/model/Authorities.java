package nl.globalorange.compliancewise.auth.domain.model;

public enum Authorities {
    USER,
    ADMIN;

    public String securityName() {
        return String.format("ROLE_%s", this);
    }
}

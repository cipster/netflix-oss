package ro.softwaredulce.dulce.auth.domain.model;

public enum Authority {
    USER,
    ADMIN;

    public String securityName() {
        return String.format("ROLE_%s", this);
    }
}

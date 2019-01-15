package nl.globalorange.compliancewise.core.multitenancy;

import org.hibernate.context.spi.CurrentTenantIdentifierResolver;

import java.util.Objects;
import java.util.Optional;

public class GrubTenantIdentifierResolver implements CurrentTenantIdentifierResolver {

    // The value returned when no tenant is actually set is the empty string.
    // This is used during boot when all the repositories are configured, including
    // the tenants, but no tenant can be considered active at that time
    static final String DUMMY_TENANT_ID = "";

    @Override
    public String resolveCurrentTenantIdentifier() {
        Optional<String> currentTenantIdentifier = CurrentTenantIdentifier.getActiveTenant();

        return currentTenantIdentifier.orElse(DUMMY_TENANT_ID);
    }

    @Override
    public boolean validateExistingCurrentSessions() {
        return true;
    }

    public boolean isTenantActive() {
        return !Objects.equals(DUMMY_TENANT_ID, resolveCurrentTenantIdentifier());
    }

    public static class CurrentTenantIdentifier {

        private static final ThreadLocal<String> ACTIVE_TENANT = new ThreadLocal<>();

        private CurrentTenantIdentifier() {
            // private default constructor to avoid unnecessary instances
        }

        /**
         * Returns an {@link Optional} with the active tenantId (empty if none)
         */
        public static Optional<String> getActiveTenant() {
            return Optional.ofNullable(ACTIVE_TENANT.get());
        }

        /**
         * Sets the active tenant
         */
        public static void setActiveTenant(String tenantId) {
            ACTIVE_TENANT.set(tenantId);
        }

        /**
         * Removes any previous value for the active tenant
         */
        public static void clearActiveTenant() {
            ACTIVE_TENANT.remove();
        }
    }
}

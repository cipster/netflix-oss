package nl.globalorange.compliancewise.core.multitenancy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static nl.globalorange.compliancewise.core.multitenancy.GrubTenantIdentifierResolver.CurrentTenantIdentifier;
import static nl.globalorange.compliancewise.core.multitenancy.GrubTenantIdentifierResolver.DUMMY_TENANT_ID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GrubTenantIdentifierResolverTest {

    private GrubTenantIdentifierResolver classUnderTest;

    @BeforeEach
    void setUp() {
        classUnderTest = new GrubTenantIdentifierResolver();
        CurrentTenantIdentifier.setActiveTenant("randomTenantId");
    }

    @Test
    void resolveCurrentTenantIdentifierWhenNoActiveTenant() {
        CurrentTenantIdentifier.setActiveTenant(null);

        String result = classUnderTest.resolveCurrentTenantIdentifier();

        assertEquals(DUMMY_TENANT_ID, result);
    }

    @Test
    void resolveCurrentTenantIdentifierWhenTenantIsSet() {
        String result = classUnderTest.resolveCurrentTenantIdentifier();

        assertEquals("randomTenantId", result);
    }

    @Test
    void validateExistingCurrentSessionsReturnsTrue() {
        boolean result = classUnderTest.validateExistingCurrentSessions();

        assertTrue(result);
    }

    @Test
    void isTenantActiveReturnsTheStateFromTheTenantIdentifier() {
        assertTrue(classUnderTest.isTenantActive());

        CurrentTenantIdentifier.clearActiveTenant();

        assertFalse(classUnderTest.isTenantActive());
    }
}
package nl.globalorange.compliancewise.core.multitenancy;

import nl.globalorange.compliancewise.core.helpers.ApplicationContextHelper;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.engine.jdbc.connections.spi.AbstractMultiTenantConnectionProvider;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.hibernate.service.UnknownUnwrapTypeException;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class SchemaMultiTenantConnectionProvider implements MultiTenantConnectionProvider {

    private static final String SET_SCHEMA_STATEMENT = "SET SCHEMA '%s';";
    private final String tenantDataSourceBeanName;
    private final String mainSchema;

    public SchemaMultiTenantConnectionProvider(String tenantDataSourceBeanName, String mainSchema) {
        this.tenantDataSourceBeanName = tenantDataSourceBeanName;
        this.mainSchema = mainSchema;
    }

    @Override
    public Connection getAnyConnection() throws SQLException {
        DataSource dataSource = getTenantDataSource();
        return dataSource.getConnection();
    }

    @Override
    public void releaseAnyConnection(Connection connection) throws SQLException {
        connection.close();
    }

    /**
     * The working schema is changed via the SET SCHEMA postgres statement.
     */
    @Override
    public Connection getConnection(String tenantIdentifier) throws SQLException {
        if (StringUtils.isBlank(tenantIdentifier)) {
            throw new IllegalStateException("No schema specified for tenant connection");
        }

        if (mainSchema.equals(tenantIdentifier)) {
            throw new IllegalStateException("A tenant connection cannot use the main schema");
        }

        final Connection connection = getAnyConnection();

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(String.format(SET_SCHEMA_STATEMENT, tenantIdentifier));
        } catch (SQLException e) {
            throw new HibernateException(String.format("Could not alter JDBC connection to specified schema [%s]", tenantIdentifier), e);
        }

        return connection;
    }

    @Override
    public void releaseConnection(String tenantIdentifier, Connection connection) throws SQLException {
        releaseAnyConnection(connection);
    }

    @Override
    public boolean supportsAggressiveRelease() {
        return true;
    }

    @Override
    public boolean isUnwrappableAs(Class unwrapType) {
        return MultiTenantConnectionProvider.class.equals(unwrapType) ||
                AbstractMultiTenantConnectionProvider.class.isAssignableFrom(unwrapType);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T unwrap(Class<T> unwrapType) {
        if (isUnwrappableAs(unwrapType)) {
            return (T) this;
        } else {
            throw new UnknownUnwrapTypeException(unwrapType);
        }
    }

    /**
     * Gets the tenant {@link DataSource} bean.
     * Used in a different separated method for testing purposes.
     */
    DataSource getTenantDataSource() {
        return (DataSource) ApplicationContextHelper.getBean(tenantDataSourceBeanName);
    }
}

package nl.globalorange.compliancewise.parties.domain.model.formly;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import org.hibernate.HibernateException;
import org.hibernate.annotations.common.util.ReflectHelper;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.usertype.ParameterizedType;
import org.hibernate.usertype.UserType;
import org.postgresql.util.PGobject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLSyntaxErrorException;
import java.sql.Types;
import java.util.List;
import java.util.Properties;

/**
 * Custom Hibernate type that saves an object as jsonb. The type of the object *must* be configured by setting
 * the {@code RETURNED_CLASS_TYPE} property when using this type. The property is a comma delimited string of
 * types, allowing the user to specify generics also.
 * For example, to specify a :
 * - untyped List             use: "java.util.List"
 * - Integer                  use: "java.lang.Integer"
 * - List&lt;Integer&gt;      use: "java.util.List, java.util.List, java.lang.Integer"
 * - ArrayList&lt;Integer&gt; use: "java.util.ArrayList, java.util.List, java.lang.Integer"
 * <p>
 * Internally {@link TypeFactory#constructParametrizedType(Class, Class, Class[])} is used to create composite types
 * so more can be found there.
 * <p>
 * Note: Saving objects as Json can also be implemented using {@link javax.persistence.Converter} but due to Postgres
 * handling of casts, either set {@code stringtype=unspecified} to hibernate properties or use the pgjdbc-ng driver
 */
public class PgJsonType implements UserType, ParameterizedType {
    public static final String SQL_TYPE_NAME = "jsonb";
    public static final String RETURNED_CLASS_TYPE = "returnedClassType";
    private static final Logger LOG = LoggerFactory.getLogger(PgJsonType.class);
    private static final ObjectMapper mapper = new ObjectMapper();

    static {
        // this will prevent a runtime exception to be thrown if one of the property in the json is an empty object
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapper.registerModule(new JavaTimeModule());

        mapper.configure(DeserializationFeature.USE_LONG_FOR_INTS, true);
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

    }

    private int sqlType = Types.JAVA_OBJECT;
    private List<Class> typeParameters;

    /**
     * {@inheritDoc}
     */
    @Override
    public int[] sqlTypes() {
        return new int[]{sqlType};
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Class returnedClass() {
        //the first type is the overall returned value (eg: List<Integer> -> List)
        return typeParameters.get(0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object x, Object y) {
        return Objects.equal(x, y);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode(Object x) {
        return Objects.hashCode(x);
    }

    @Override
    public Object nullSafeGet(ResultSet rs, String[] names, SharedSessionContractImplementor session, Object owner) throws HibernateException, SQLException {
        Preconditions.checkArgument(sqlType == Types.JAVA_OBJECT);
        Object result = null;

        String content = rs.getString(names[0]);
        if (content != null) {
            try {
                result = mapper.readValue(content.getBytes(StandardCharsets.UTF_8), classType());
            } catch (IOException e) {
                throw new HibernateException("unable to read object", e);
            }
        }

        return result;
    }

    @Override
    public void nullSafeSet(PreparedStatement st, Object value, int index, SharedSessionContractImplementor session) throws HibernateException, SQLException {
        Preconditions.checkArgument(sqlType == Types.JAVA_OBJECT);
        if (value == null) {
            st.setNull(index, Types.NULL);
            return;
        }

        try {
            PGobject data = new PGobject();
            data.setType(SQL_TYPE_NAME);
            data.setValue(mapper.writeValueAsString(value));
            writePgObjectToStatement(data, st, index);
        } catch (JsonProcessingException e) {
            throw new HibernateException("unable to set object", e);
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Object deepCopy(Object value) {
        if (value != null) {
            try {
                return mapper.readValue(mapper.writeValueAsString(value), classType());
            } catch (IOException e) {
                throw new HibernateException("unable to deep copy object", e);
            }
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isMutable() {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Serializable disassemble(Object value) {
        return (Serializable) this.deepCopy(value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object assemble(Serializable cached, Object owner) {
        return this.deepCopy(cached);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object replace(Object original, Object target, Object owner) {
        return this.deepCopy(original);
    }

    /**
     * {@inheritDoc}
     * <p>
     * This needs to received the class of the returned object using {@code RETURNED_CLASS_TYPE}. Without it, deserialization
     * of objects will not work. The property is a comma delimited string of types, allowing the user to specify generics also.
     * For example, to specify a :
     * - untyped List             use: "java.util.List"
     * - Integer                  use: "java.lang.Integer"
     * - List&lt;Integer&gt;      use: "java.util.List, java.util.List, java.lang.Integer"
     * - ArrayList&lt;Integer&gt; use: "java.util.ArrayList, java.util.List, java.lang.Integer"
     * <p>
     * Internally {@link TypeFactory#constructParametrizedType(Class, Class, Class[])}  is used to create composite types
     * so more can be found there.
     */
    @Override
    public void setParameterValues(Properties parameters) {
        String returnedClassType = parameters.getProperty(RETURNED_CLASS_TYPE);
        try {
            typeParameters = Lists.newArrayList();
            for (String typeParameterName : returnedClassType.split("\\s*,\\s*")) {
                typeParameters.add(ReflectHelper.classForName(typeParameterName, this.getClass()));
            }

            if (typeParameters.isEmpty() || typeParameters.size() == 2) {
                throw new HibernateException("invalid number of parameters provided to returnedClassType: " + returnedClassType);
            }

        } catch (ClassNotFoundException e) {
            throw new HibernateException("classType not found", e);
        }
    }

    /**
     * Constructs a {@link JavaType} from the {@literal typeParameters} provided to this instance. This in turn is
     * used to control the exact type Jackson will serialize the jsonb data from the database.
     * Depending on the number of paramters supplied, this will construct either a simple type using
     * {@link TypeFactory#constructType(Type)} if only one argument was supplied, or
     * {@link TypeFactory#constructParametrizedType(Class, Class, Class[])} if more.
     *
     * @return the type Jackson should serialize the value to
     */
    private JavaType classType() {
        TypeFactory typeFactory = mapper.getTypeFactory();
        Class parametrized = typeParameters.get(0);

        if (typeParameters.size() == 1) {
            return typeFactory.constructType(parametrized);
        } else {
            Class parameterFor = typeParameters.get(1);
            Class[] parameterClasses = typeParameters.stream().skip(2).toArray(Class[]::new);
            return typeFactory.constructParametrizedType(parametrized, parameterFor, parameterClasses);
        }
    }

    /**
     * Write to a prepared statement. We try to write it directly as a pg object class. This can fail when using
     * HQSLDB, in which case we will try to write the data as string.
     *
     * @param data  the pg object to write
     * @param st    a JDBC prepared statement
     * @param index statement parameter index
     */
    private void writePgObjectToStatement(PGobject data, PreparedStatement st, int index) throws SQLException {
        try {
            st.setObject(index, data);
        } catch (SQLSyntaxErrorException e) {
            //this only happens when using HSQLDB since it does not know of the PGObject type, we need to send
            //the data as string
            LOG.warn("Could not write {} data as PGObject, assuming HSQLDB and falling back to string", SQL_TYPE_NAME);
            st.setString(index, data.getValue());
        }
    }
}

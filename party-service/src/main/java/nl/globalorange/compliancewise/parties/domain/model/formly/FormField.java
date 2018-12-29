package nl.globalorange.compliancewise.parties.domain.model.formly;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class FormField implements Serializable {
    private String key;
    private String type;

    /**
     * Using {@link HashMap} instead of {@link Map} to make the property {@link Serializable}
     */
    private HashMap<String, Object> templateOptions;

    /**
     * This field contains all other properties which are sent to the backend but not specifically mapped into the class.
     * Using {@link HashMap} instead of {@link Map} to make the property {@link Serializable}
     */
    private HashMap<String, Object> extraProperties = new HashMap<>();

    /**
     * Return the field's key
     *
     * @return key
     */
    public String getKey() {
        return key;
    }

    /**
     * Sets the field key
     *
     * @param key key
     */
    public void setKey(String key) {
        this.key = key;
    }

    /**
     * Return the type of the form field (eg: input, checkbox etc.)
     *
     * @return the form type
     */
    public String getType() {
        return type;
    }

    /**
     * Set the type of the form field
     *
     * @param type the form type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Return the template options, these are specific to each field type
     *
     * @return the options
     */
    public Map<String, Object> getTemplateOptions() {
        return templateOptions;
    }

    /**
     * Set the template options, these are specific to each field type
     *
     * @param templateOptions the options
     */
    public void setTemplateOptions(Map<String, Object> templateOptions) {
        this.templateOptions = templateOptions != null ? new HashMap<>(templateOptions) : null;
    }

    /**
     * Getter for {@link #extraProperties}
     */
    @JsonAnyGetter
    public Map<String, Object> getExtraProperties() {
        return extraProperties;
    }

    /**
     * Sets a single property for {@link #extraProperties}
     */
    @JsonAnySetter
    public void setExtraProperties(String key, Object value) {
        this.extraProperties.put(key, value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getKey(), getType(), getTemplateOptions(), getExtraProperties());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof FormField))
            return false;
        FormField formField = (FormField) o;
        return Objects.equals(getKey(), formField.getKey()) &&
                Objects.equals(getType(), formField.getType()) &&
                Objects.equals(getTemplateOptions(), formField.getTemplateOptions()) &&
                Objects.equals(getExtraProperties(), formField.getExtraProperties());
    }
}

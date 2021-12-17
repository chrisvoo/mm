package models;

import exceptions.ModelException;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public abstract class Model {
    private static final Logger logger = Logger.getLogger(Model.class.getName());
    protected transient List<String> requiredFields;
    protected transient Map<String, String> errors = new HashMap<>();
    protected transient Integer errorCode = null;

    public boolean isValid() {
        return this.areRequiredFieldsValid();
    }

    public Map<String, String> getErrors() {
        return this.errors;
    }

    public Integer getErrorCode() {
        return this.errorCode;
    }

    protected void positiveNumberValidator(String fieldName, Short num) {
        if (num != null) {
            this.positiveNumberValidator(fieldName, num.intValue());
        }
    }

    protected void positiveNumberValidator(String fieldName, Long num) {
        if (num != null) {
            this.positiveNumberValidator(fieldName, num.intValue());
        }
    }

    protected void positiveNumberValidator(String fieldName, Integer num) {
        if (num != null && num < 0) {
            this.errorCode = ModelException.INVALID_FIELDS;
            this.errors.put(fieldName, "Must be greater than or equal to 0");
        }
    }

    protected void lengthValidator(String fieldName, String fieldValue, int length) {
        if (fieldValue != null && fieldValue.length() > length) {
            this.errorCode = ModelException.INVALID_FIELDS;
            this.errors.put(fieldName, "Length superior to " + length);
        }
    }

    /**
     * Just iterates the subclass' fields and checks the not-null constraint for those that are
     * specified inside {@link #requiredFields}
     * @return true if
     */
    private boolean areRequiredFieldsValid() {
        // every time this method is called, the state must be reset.
        this.errors.clear();
        this.errorCode = null;

        if (requiredFields == null || requiredFields.isEmpty()) {
            return true;
        }

        for (Field field : this.getClass().getDeclaredFields()) {
            if (requiredFields.contains(field.getName())) {
                Type type = field.getGenericType();
                try {
                    field.setAccessible(true);
                    Object value = field.get(this);
                    boolean status = false;

                    if (type.getTypeName().equals("java.lang.String")) {
                        status = value != null && value.toString().trim().length() > 0;
                    } else {
                        status = value != null;
                    }

                    if (!status) {
                        this.errorCode = ModelException.MISSING_REQUIRED_FIELDS;
                        this.errors.put(field.getName(), "missing or null");
                        return false;
                    }
                } catch (IllegalAccessException e) {
                    logger.severe(
                        String.format(
                            "Validation failed for %s: %s",
                            this.getClass().getName(),
                            e.getMessage()
                        )
                    );
                }
            }
        }

        return true;
    }
}
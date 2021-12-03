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
    protected List<String> requiredFields;
    protected Map<String, String> errors = new HashMap<>();
    protected Integer errorCode = null;

    public boolean isValid() {
        return this.areRequiredFieldsValid();
    }

    public Map<String, String> getErrors() {
        return this.errors;
    }

    public Integer getErrorCode() {
        return this.errorCode;
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
    public boolean areRequiredFieldsValid() {
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

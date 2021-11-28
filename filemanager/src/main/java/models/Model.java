package models;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.List;
import java.util.logging.Logger;

public abstract class Model {
    private static final Logger logger = Logger.getLogger(Model.class.getName());
    protected List<String> requiredFields;

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
                System.out.println(type.getTypeName());
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

package form.utils;

import form.FormProperty;
import form.validator.ValidateProperty;
import form.validator.Validator;
import form.validator.ValidatorType;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ReflectionUtils {

    public static void fillFields(Class clazz, List<Field> result) {
        if (clazz.getSuperclass() != null && clazz.getSuperclass() != Object.class) {
            fillFields(clazz.getSuperclass(), result);
        }

        for (Field field : clazz.getDeclaredFields()) {
            if (isFormProperty(field)) {
                field.setAccessible(true);
                result.add(field);
            }
        }

    }

    public static List<Field> listFields(Class clazz) {
        List<Field> result = new LinkedList<>();
        fillFields(clazz, result);
        return result;
    }

    public static boolean isFormProperty(Field field) {
        return field.getAnnotationsByType(FormProperty.class).length > 0;
    }

    public static List<Validator<?>> getValidators(Field field) {
        List<Validator<?>> validators = new ArrayList<>();
        for (Annotation annotation: field.getAnnotationsByType(ValidateProperty.class)) {
            Class<? extends Annotation> type = annotation.annotationType();
            try {
                Method method = type.getDeclaredMethod("types");
                ValidatorType[] validatorTypes = (ValidatorType[])method.invoke(annotation);
                for (int i=0; i<validatorTypes.length; i++)
                    validators.add(validatorTypes[i].getValidator());
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        return validators;
    }

}

package form.validator;

import java.lang.annotation.*;

//  @ValidateProperty(types = ValidatorType.NAME)
//  @ValidateProperty(types = {ValidatorType.NAME, ValidatorType.XXX ... })

@Inherited
@Target(value= ElementType.FIELD)
@Retention(value = RetentionPolicy.RUNTIME)
public @interface ValidateProperty {
    ValidatorType[] types();
}

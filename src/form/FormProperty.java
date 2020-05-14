package form;

import java.lang.annotation.*;

@Inherited
@Target(value= ElementType.FIELD)
@Retention(value = RetentionPolicy.RUNTIME)
public @interface FormProperty {
}

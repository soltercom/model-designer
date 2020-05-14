package form.validator;

import java.util.regex.Pattern;

public class NameValidator implements Validator<String> {

    private final String pattern = "^[a-zA-Zа-яА-Я_][a-zA-Z0-9_а-яА-Я]*$";

    @Override
    public boolean validate(String value) {
        return Pattern.compile(pattern).matcher(value).matches();
    }
}

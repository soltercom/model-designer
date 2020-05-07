package formsfx.model.validator;

import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class RegexValidator extends RootValidator<String> {

    private Pattern pattern;

    private RegexValidator(String pattern, String errorMessage) throws PatternSyntaxException {
        super(errorMessage);
        this.pattern = Pattern.compile(pattern);
    }

    public static RegexValidator forPattern(String pattern, String errorMessage) {
        return new RegexValidator(pattern, errorMessage);
    }

    public static RegexValidator forNames(String errorMessage) {
        return new RegexValidator("^[a-zA-Zа-яА-Я_][a-zA-Z0-9_а-яА-Я]*$", errorMessage);
    }

    @Override
    public ValidationResult validate(String input) {
        return createResult(pattern.matcher(input).matches());
    }


}

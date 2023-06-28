package backend.model.validators;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static backend.util.ExchangeAppUtils.BAD_EMAIL_ERROR;

public class EmailPatternMatcher {

    private List<String> acceptedEmailSuffixes;

    public EmailPatternMatcher(List<String> acceptedEmailSuffixes) {
        this.acceptedEmailSuffixes = acceptedEmailSuffixes;
    }

    public List<String> validate(String email) {
        for(String suffix : acceptedEmailSuffixes) {
            Pattern pattern = Pattern.compile(suffix + "$", Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(email);
            if(matcher.find()) {
                return List.of();
            }
        }
        return List.of(BAD_EMAIL_ERROR);
    }
}

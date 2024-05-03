package main.validators;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.Period;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class UserValidator {
    private static final String EMAIL_REGEX =
            "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
    private static final String PHONE_REGEX =
            "^(?:(?:\\+|00)([1-9]|\\d{2,3}))?[-. (]*(\\d{3})[-. )]*(\\d{3})[-. ]*(\\d{4})$";
    public boolean emailValidation(String email){
        return regexValidation(email,EMAIL_REGEX);
    }
    public boolean isEarlierThenCurrentDateValidation(LocalDate birthDate){
        return birthDate.isBefore(LocalDate.now());
    }
    public boolean phoneNumberValidation(String phoneNumber){
        return regexValidation(phoneNumber,PHONE_REGEX);
    }
    public boolean regexValidation(String input,String regex){
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        return matcher.matches();
    }
}

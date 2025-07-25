/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ke.co.mspace.nonsmppmanager.validators;

/**
 *
 * @author brian
 */
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class MultiEmailValidator implements ConstraintValidator<MultiEmail, String> {

    private static final Pattern EMAIL_REGEX =
            Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    @Override
    public void initialize(MultiEmail constraintAnnotation) {
        // no initialization needed
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.trim().isEmpty()) {
            return false; // you can customize this to allow empty if needed
        }

        String[] emails = value.split(",");

        for (String email : emails) {
            if (!EMAIL_REGEX.matcher(email.trim()).matches()) {
                return false;
            }
        }

        return true;
    }
}


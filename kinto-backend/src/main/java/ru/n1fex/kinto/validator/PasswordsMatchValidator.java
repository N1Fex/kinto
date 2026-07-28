package ru.n1fex.kinto.validator;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ru.n1fex.kinto.annotation.PasswordsMatch;
import ru.n1fex.kinto.dto.RegistrationRequest;

import java.util.Objects;

public class PasswordsMatchValidator implements ConstraintValidator<PasswordsMatch, RegistrationRequest> {
    @Override
    public boolean isValid(RegistrationRequest request, ConstraintValidatorContext constraintValidatorContext) {
        return Objects.equals(request.getPassword(), request.getConfirmPassword());
    }

}

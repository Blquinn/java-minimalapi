package com.github.blquinn.common.modules;

import com.github.blquinn.common.modules.validation.ValidationErrorDto;
import com.github.blquinn.common.modules.validation.ValidationErrorResponseDto;
import com.github.blquinn.common.modules.validation.ValidationException;
import io.jooby.Context;
import io.jooby.Extension;
import io.jooby.Jooby;
import io.jooby.MediaType;
import io.jooby.MessageDecoder;
import io.jooby.json.JacksonModule;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Path;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import java.lang.reflect.Type;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import javax.annotation.Nonnull;

public class ValidationModule implements Extension {
  private final JacksonModule jacksonModule;

  public ValidationModule(JacksonModule jacksonModule) {
    this.jacksonModule = jacksonModule;
  }

  @Override
  public void install(@Nonnull Jooby application) throws Exception {
    var validator = Validation.buildDefaultValidatorFactory().getValidator();
    application.getServices().putIfAbsent(Validator.class, validator);
    application.decoder(MediaType.json,
        validatedJacksonDecoder(jacksonModule, application.getServices().require(Validator.class)));
  }

  static MessageDecoder validatedJacksonDecoder(JacksonModule jacksonModule, Validator validator) {
    return new MessageDecoder() {
      @Nonnull
      @Override
      public Object decode(@Nonnull Context ctx, @Nonnull Type type) throws Exception {
        var obj = jacksonModule.decode(ctx, type);
        var errs = validator.validate(obj);
        if (!errs.isEmpty()) {
          throw new ValidationException(convertConstrainViolation(errs));
        }
        return obj;
      }

      private static <T> ValidationErrorResponseDto convertConstrainViolation(
          Set<ConstraintViolation<T>> violations) {

        var validationErrs = violations.stream().map(v -> {
          String path = StreamSupport.stream(v.getPropertyPath().spliterator(), false)
              .map(Path.Node::toString)
              .collect(Collectors.joining("."));
          return new ValidationErrorDto(path, v.getMessage());
        }).toList();

        return new ValidationErrorResponseDto("One or more fields were invalid.", validationErrs);
      }
    };
  }
}

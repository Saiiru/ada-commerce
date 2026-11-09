package com.ada.commerce.model.vo;

import com.ada.commerce.model.exception.InvalidDocumentException;
import java.util.Locale;
import java.util.Objects;
import java.util.regex.Pattern;

public final class ValidarEmail {
  private static final Pattern SIMPLE = Pattern.compile(
      "^[A-Za-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[A-Za-z0-9!#$%&'*+/=?^_`{|}~-]+)*@"
      + "(?:[A-Za-z0-9](?:[A-Za-z0-9-]*[A-Za-z0-9])?\\.)+[A-Za-z]{2,}$");
  private final String value;

  public ValidarEmail(String value) throws InvalidDocumentException {
    if (value == null || !SIMPLE.matcher(value).matches())
      throw new InvalidDocumentException("E-mail inválido");
    this.value = value.toLowerCase(Locale.ROOT);
  }

  public String value() { return value; }
  @Override public boolean equals(Object o) { return o instanceof ValidarEmail email && value.equals(email.value); }
  @Override public int hashCode() { return Objects.hash(value); }
  @Override public String toString() { return value; }
}

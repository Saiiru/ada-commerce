package com.ada.commerce.model.vo;

import com.ada.commerce.model.exception.InvalidDocumentException;

import java.util.Objects;
import java.util.regex.Pattern;

public final class ValidarEmail {
  private static final Pattern SIMPLE = Pattern.compile("^[\\\\w!#$%&’*+/=?`{|}~^.-]+(?:\\\\.[\\\\w!#$%&’*+/=?`{|}~^.-]+)*@(?:[a-zA-Z0-9-]+\\\\.)+[a-zA-Z]{2,6}$");
  private final String value;

  public ValidarEmail(String value) throws InvalidDocumentException {
    if (value == null || !SIMPLE.matcher(value).matches())
      throw new InvalidDocumentException("E-mail inválido");
    this.value = value.toLowerCase();
  }

  public String value(){ return value; }

  @Override public boolean equals(Object o){ return o instanceof ValidarEmail && value.equals(((ValidarEmail)o).value); }

  @Override public int hashCode(){ return Objects.hash(value); }

  @Override public String toString(){ return value; }
}

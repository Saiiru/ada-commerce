package com.ada.commerce.model.vo;

import com.ada.commerce.model.exception.InvalidDocumentException;

import java.util.Objects;
import java.util.regex.Pattern;

public final class Email {
  private static final Pattern SIMPLE = Pattern.compile("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");
  private final String value;

  public Email(String value) throws InvalidDocumentException {
    if (value == null || !SIMPLE.matcher(value).matches())
      throw new InvalidDocumentException("E-mail inválido");
    this.value = value.toLowerCase();
  }

  public String value(){ return value; }

  @Override public boolean equals(Object o){ return o instanceof Email && value.equals(((Email)o).value); }

  @Override public int hashCode(){ return Objects.hash(value); }

  @Override public String toString(){ return value; }
}

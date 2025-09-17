package com.ada.commerce.model.vo;

import com.ada.commerce.model.exception.InvalidDocumentException;

import java.util.Locale;
import java.util.Objects;


public final class Document {
  private final String value;
  private final Type type;

  public enum Type { CPF, CNPJ }

  public Document(String raw, Type type) throws InvalidDocumentException {
    if (raw == null) throw new InvalidDocumentException("Documento nulo");
    this.type = Objects.requireNonNull(type, "type");
    DocumentValidator validator = validatorFor(type);

    if (!validator.isValid(raw)) {
      throw new InvalidDocumentException("Documento inválido para tipo " + type);
    }
    String cleaned = raw.replaceAll("\\D", "");
    if (type == Type.CPF && cleaned.length() != 11) throw new InvalidDocumentException("CPF com tamanho inválido");
    if (type == Type.CNPJ && cleaned.length() != 14) throw new InvalidDocumentException("CNPJ com tamanho inválido");

    this.value = cleaned;
  }

  private DocumentValidator validatorFor(Type type) {
    return switch (type) {
      case CPF -> new ValidarCPF();
      case CNPJ -> new ValidarCNPJ();
    };
  }

  public String value() { return value; }

  public Type type() { return type; }

  public String formatted() {
    if (type == Type.CPF && value.length() == 11) {
      return String.format("%s.%s.%s-%s",
        value.substring(0,3),
        value.substring(3,6),
        value.substring(6,9),
        value.substring(9));
    } else if (type == Type.CNPJ && value.length() == 14) {
      return String.format("%s.%s.%s/%s-%s",
        value.substring(0,2),
        value.substring(2,5),
        value.substring(5,8),
        value.substring(8,12),
        value.substring(12));
    }
    return value;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Document)) return false;
    Document d = (Document) o;
    return value.equals(d.value) && type == d.type;
  }

  @Override
  public int hashCode() {
    return Objects.hash(value, type);
  }

  @Override
  public String toString() {
    return String.format(Locale.ROOT, "%s(%s)", formatted(), type);
  }
}

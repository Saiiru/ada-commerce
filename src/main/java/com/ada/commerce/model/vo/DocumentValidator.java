package com.ada.commerce.model.vo;

public interface DocumentValidator {
  /**
   * Valida o valor (pode ser formatado ou só dígitos).
   * @param value entrada do usuário (ex: "123.456.789-09" ou "12345678909")
   * @return true se válido
   */
  boolean isValid(String value);
}

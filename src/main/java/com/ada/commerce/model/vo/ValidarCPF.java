package com.ada.commerce.model.vo;

import java.util.Objects;

public class ValidarCPF implements DocumentValidator {
  private static final String CPF_FORMATTED_REGEX = "^\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}$";
  private static final String ONLY_DIGITS = "^\\d{11}$";

  @Override
  public boolean isValid(String valor) {
    if (valor == null) return false;
    String v = valor.trim();
    if (!v.matches(CPF_FORMATTED_REGEX) && !v.matches(ONLY_DIGITS)) return false;

    String cpf = v.replaceAll("\\D", "");
    if (cpf.length() != 11) return false;
    if (cpf.chars().distinct().count() == 1) return false; // sequências repetidas inválidas

    int dig1 = calcularDigito(cpf, 9, 10);
    int dig2 = calcularDigito(cpf, 10, 11);

    int expected1 = Character.getNumericValue(cpf.charAt(9));
    int expected2 = Character.getNumericValue(cpf.charAt(10));
    return dig1 == expected1 && dig2 == expected2;
  }

  private int calcularDigito(String cpf, int length, int pesoInicial) {
    int soma = 0;
    for (int i = 0; i < length; i++) {
      soma += Character.getNumericValue(cpf.charAt(i)) * (pesoInicial - i);
    }
    int resto = soma % 11;
    return (resto < 2) ? 0 : 11 - resto;
  }

  @Override
  public boolean equals(Object o) {
    return o instanceof ValidarCPF;
  }

  @Override
  public int hashCode() { return Objects.hash("ValidarCPF"); }
}

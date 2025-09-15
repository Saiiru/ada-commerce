package com.ada.commerce.model.vo;

import java.util.Objects;

public class ValidarCNPJ implements DocumentValidator {

  private static final String CNPJ_FORMATTED_REGEX = "^\\d{2}\\.\\d{3}\\.\\d{3}/\\d{4}-\\d{2}$";
  private static final String ONLY_DIGITS = "^\\d{14}$";

  @Override
  public boolean isValid(String valor) {
    if (valor == null) return false;
    String v = valor.trim();
    if (!v.matches(CNPJ_FORMATTED_REGEX) && !v.matches(ONLY_DIGITS)) return false;

    String cnpj = v.replaceAll("\\D", "");
    if (cnpj.length() != 14) return false;
    if (cnpj.chars().distinct().count() == 1) return false;

    int dig1 = calcularDigito(cnpj, 12, new int[]{5,4,3,2,9,8,7,6,5,4,3,2});
    int dig2 = calcularDigito(cnpj, 13, new int[]{6,5,4,3,2,9,8,7,6,5,4,3,2});

    int expected1 = Character.getNumericValue(cnpj.charAt(12));
    int expected2 = Character.getNumericValue(cnpj.charAt(13));
    return dig1 == expected1 && dig2 == expected2;
  }

  private int calcularDigito(String cnpj, int length, int[] pesos) {
    int soma = 0;
    for (int i = 0; i < length; i++) {
      soma += Character.getNumericValue(cnpj.charAt(i)) * pesos[i];
    }
    int resto = soma % 11;
    return (resto < 2) ? 0 : 11 - resto;
  }

  @Override
  public boolean equals(Object o) {
    return o instanceof ValidarCNPJ;
  }

  @Override
  public int hashCode() { return Objects.hash("ValidarCNPJ"); }
}

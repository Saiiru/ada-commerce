package com.ada.commerce.model.exception;

public class DuplicateDocumentException extends RuntimeException {
  public DuplicateDocumentException(String message) {
    super(message);
  }
}

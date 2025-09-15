package com.ada.commerce.model.exception;

public class InvalidDocumentException extends Throwable {
  public InvalidDocumentException(String message) { super(message); }
  public InvalidDocumentException(String message, Throwable cause) { super(message, cause); }
}

package bluefoodApp.service;

public class PagamentoException extends Exception{

  public PagamentoException() {
  }

  public PagamentoException(String message, Throwable cause) {
    super(message, cause);
  }

  public PagamentoException(Throwable cause) {
    super(cause);
  }

  public PagamentoException(String message) {
    super(message);
  }
}

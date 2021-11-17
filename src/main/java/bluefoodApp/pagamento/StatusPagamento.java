package bluefoodApp.pagamento;

public enum StatusPagamento {

  Autorizado("Autorizado"),
  NaoAutorizado("Não autorizado pela instituição fincanceira"),
  CartaoInvalido("Cartão inválido ou bloqueado");

  String descricao;

  StatusPagamento(String descricao) {
    this.descricao = descricao;
  }

  public String getDescricao() {
    return this.descricao;
  }
}

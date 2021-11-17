package bluefoodApp.pagamento;

import bluefoodApp.pedido.Pedido;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "pagamento")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Pagamento implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @OneToOne
  @NotNull
  @MapsId
  private Pedido pedido;

  @NotNull
  private LocalDateTime data;

  @Column(name = "num_cartao_final")
  @NotNull
  @Size(min = 4, max = 4)
  private String numCartaoFinal;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 10)
  private BandeiraCartao bandeiraCartao;

  public void definirNumeroBandeira(String numCartao) {
    numCartaoFinal = numCartao.substring(12);
    bandeiraCartao = getBandeira(numCartaoFinal);
  }

  private BandeiraCartao getBandeira(String numCartao) {
    if (numCartao.startsWith("0000")) {
      return BandeiraCartao.AMEX;
    }
    if (numCartao.startsWith("1111")) {
      return BandeiraCartao.ELO;
    }
    if (numCartao.startsWith("2222")) {
      return BandeiraCartao.MASTER;
    }
    return BandeiraCartao.VISA;
  }

}

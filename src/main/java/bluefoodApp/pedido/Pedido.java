package bluefoodApp.pedido;

import bluefoodApp.cliente.Cliente;
import bluefoodApp.pagamento.Pagamento;
import bluefoodApp.restaurante.Restaurante;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "pedido")
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Pedido implements Serializable {

  public enum Status {
    Producao(1, "Em produção", false),
    Entrega(2, "Saiu para entrega", false),
    Concluido(3, "Concluído", true);

    Status(int ordem, String descricao, boolean ultimo) {
      this.ordem = ordem;
      this.descricao = descricao;
      this.ultimo = ultimo;
    }

    int ordem;
    String descricao;
    boolean ultimo;

    public String getDescricao() {
      return descricao;
    }

    public int getOrdem() {
      return ordem;
    }

  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @NotNull
  private LocalDateTime data;

  @NotNull
  private Status status;

  @NotNull
  @ManyToOne
  private Cliente cliente;

  @NotNull
  @ManyToOne
  private Restaurante restaurante;

  @NotNull
  private BigDecimal subtotal;

  @NotNull
  @Column(name = "taxa_entrega")
  private BigDecimal taxaEntrega;

  @NotNull
  private BigDecimal total;

  @OneToMany(mappedBy = "id.pedido", fetch = FetchType.EAGER)
  private Set<ItemPedido> itens;

  @OneToOne(mappedBy = "pedido")
  private Pagamento pagamento;

  public String formattedId() {
    return String.format("#%04d", id);
  }
}

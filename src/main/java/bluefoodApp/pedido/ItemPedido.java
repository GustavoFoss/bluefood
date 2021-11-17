package bluefoodApp.pedido;

import bluefoodApp.restaurante.ItemCardapio;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;


@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "item_pedido")
public class ItemPedido implements Serializable {

  @EqualsAndHashCode.Include
  @EmbeddedId
  private ItemPedidoPk id;

  @NotNull
  @ManyToOne
  private ItemCardapio itemCardapio;

  @NotNull
  private Integer quantidade;

  @Size(max = 50)
  private String observacoes;

  @NotNull
  private BigDecimal preco;

  public BigDecimal getPrecoCalculado() {
    return preco.multiply(BigDecimal.valueOf(quantidade));
  }
}

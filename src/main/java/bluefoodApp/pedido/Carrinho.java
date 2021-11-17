package bluefoodApp.pedido;

import bluefoodApp.restaurante.ItemCardapio;
import bluefoodApp.restaurante.Restaurante;
import lombok.Getter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Getter
public class Carrinho implements Serializable {

  private List<ItemPedido> itens = new ArrayList<>();
  private Restaurante restaurante;

  public void adicionarItem(ItemCardapio itemCardapio, Integer quantidade, String observacoes) throws ResturanteDiferenteException {

    if (itens.size() == 0) {
      restaurante = itemCardapio.getRestaurante();
    } else if (!itemCardapio.getRestaurante().getId().equals(restaurante.getId())) {
      throw new ResturanteDiferenteException();
    }

    if (!exists(itemCardapio)) {
      ItemPedido itemPedido = new ItemPedido();

      itemPedido.setItemCardapio(itemCardapio);
      itemPedido.setQuantidade(quantidade);
      itemPedido.setObservacoes(observacoes);
      itemPedido.setPreco(itemCardapio.getPreco());

      itens.add(itemPedido);
    }
  }

  public void adicionarItem(ItemPedido itemPedido) {
    try {
      adicionarItem(itemPedido.getItemCardapio(), itemPedido.getQuantidade(), itemPedido.getObservacoes());
    } catch (ResturanteDiferenteException e) {

    }
  }

  public void removeItem(ItemCardapio itemCardapio) {
    for (Iterator<ItemPedido> iterator = itens.iterator(); iterator.hasNext(); ) {
      ItemPedido itemPedido = iterator.next();
      if (itemPedido.getItemCardapio().getId().equals(itemCardapio.getId())) {
        iterator.remove();
        break;
      }
    }

    if (itens.size() == 0) {
      restaurante = null;
    }
  }

  public boolean exists(ItemCardapio itemCardapio) {
    for (ItemPedido itemPedido : itens) {
      if (itemPedido.getItemCardapio().getId().equals(itemCardapio.getId())) {
        return true;
      }
    }
    return false;
  }

  public BigDecimal getPrecoTotal(boolean adicionarTaxaEntrega) {
    BigDecimal soma = BigDecimal.ZERO;

    for (ItemPedido itemPedido : itens) {
      soma = soma.add(itemPedido.getPrecoCalculado());
    }

    if (adicionarTaxaEntrega) {
      soma = soma.add(restaurante.getTaxaEntrega());
    }

    return soma;
  }

  public void limpar() {
    itens.clear();
    restaurante = null;
  }

  public boolean vazio() {
    return itens.size() == 0;
  }
}

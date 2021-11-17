package bluefoodApp.controllerWeb;

import bluefoodApp.pedido.*;
import bluefoodApp.restaurante.ItemCardapio;
import bluefoodApp.restaurante.ItemCardapioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;


@Controller
@RequestMapping("/cliente/carrinho")
@SessionAttributes("carrinho")
public class CarrinhoController {

  @Autowired
  private ItemCardapioRepository itemCardapioRepository;

  @ModelAttribute("carrinho")
  public Carrinho carrinho() {
    return new Carrinho();
  }

  @Autowired
  private PedidoRepository pedidoRepository;

  @GetMapping(path = "/adicionar")
  public String adicionarItem(
          @RequestParam("itenId") Integer itemId,
          @RequestParam("quantidade") Integer quantidade,
          @RequestParam("observacoes") String observacoes,
          @ModelAttribute("carrinho") Carrinho carrinho,
          Model model) {
    ItemCardapio itemCardapio = itemCardapioRepository.findById(itemId).orElseThrow();
    try {
      carrinho.adicionarItem(itemCardapio, quantidade, observacoes);
    } catch (ResturanteDiferenteException e) {
      model.addAttribute("msg", "Não é possivel misturar comidas de rest diferentes");
    }

    return "cliente-carrinho";
  }

  @GetMapping(path = "/remover")
  public String removerItem(
          @RequestParam("itenId") Integer itemId,
          @ModelAttribute("carrinho") Carrinho carrinho,
          SessionStatus sessionStatus,
          Model model
  ) {
    ItemCardapio itemCardapio = itemCardapioRepository.findById(itemId).orElseThrow();

    carrinho.removeItem(itemCardapio);

    if (carrinho.vazio()) {
      sessionStatus.setComplete();
    }

    return "cliente-carrinho";
  }

  @GetMapping(path = "/visualizar")
  public String viewCarrinho() {
    return "cliente-carrinho";
  }

  @GetMapping(path = "/refazerCarrinho")
  public String refazerCarrinho(
          @RequestParam("pedidoId") Integer pedidoId,
          @ModelAttribute("carrinho") Carrinho carrinho,
          Model model
  ) {
    Pedido pedido = pedidoRepository.findById(pedidoId).orElseThrow();

    carrinho.limpar();

    for (ItemPedido itemPedido : pedido.getItens()) {
      carrinho.adicionarItem(itemPedido);
    }

    return "cliente-carrinho";

  }

}

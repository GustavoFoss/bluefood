package bluefoodApp.controllerWeb;


import bluefoodApp.pedido.Carrinho;
import bluefoodApp.pedido.Pedido;
import bluefoodApp.service.PagamentoException;
import bluefoodApp.service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

@Controller
@RequestMapping(path = "/cliente/pagamento")
@SessionAttributes("carrinho")
public class PagamentoController {

  @Autowired
  private PedidoService pedidoService;

  @PostMapping(path = "/pagar")
  public String pagar(@RequestParam("numCartao") String cartao,
                      @ModelAttribute("carrinho") Carrinho carrinho,
                      SessionStatus sessionStatus, Model model) {

    try {
      Pedido pedido = pedidoService.criarEPagar(carrinho, cartao);
      sessionStatus.setComplete();
      return "redirect:/cliente/pedido/view?pedidoId=" + pedido.getId();
    } catch (PagamentoException e) {
      model.addAttribute("msg", e.getMessage());
      return "cliente-carrinho";
    }



  }
}

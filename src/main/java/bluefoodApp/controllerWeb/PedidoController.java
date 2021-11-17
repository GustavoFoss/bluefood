package bluefoodApp.controllerWeb;


import bluefoodApp.pedido.Pedido;
import bluefoodApp.pedido.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(path = "/cliente/pedido")
public class PedidoController {

  @Autowired
  private PedidoRepository pedidoRepository;

  @GetMapping(path = "/view")
  public String viewPedido(
          @RequestParam("pedidoId") Integer pedidoId,
          Model model
  ) {

    Pedido pedido = pedidoRepository.findById(pedidoId).orElseThrow();
    model.addAttribute("pedido", pedido);

    return "cliente-pedido";
  }
}

package bluefoodApp.controllerWeb;

import bluefoodApp.cliente.Cliente;
import bluefoodApp.cliente.ClienteRepository;
import bluefoodApp.pedido.Pedido;
import bluefoodApp.pedido.PedidoRepository;
import bluefoodApp.restaurante.*;
import bluefoodApp.service.ClienteService;
import bluefoodApp.service.RestauranteService;
import bluefoodApp.service.ValidationException;
import bluefoodApp.util.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping(path = "/cliente")
public class ClienteController {

  @Autowired
  private ClienteRepository clienteRepository;

  @Autowired
  private RestauranteService restauranteService;

  @Autowired
  private ItemCardapioRepository itemCardapioRepository;

  @Autowired
  private CategoriaRestauranteRepository categoriaRestauranteRepository;

  @Autowired
  private ClienteService clienteService;

  @Autowired
  private RestauranteRepository restauranteRepository;

  @Autowired
  private PedidoRepository pedidoRepository;

  @GetMapping(path = "/home")
  public String home(Model model) {
    List<CategoriaRestaurante> categorias = categoriaRestauranteRepository.findAll(Sort.by("nome"));

    List<Pedido> pedidos = pedidoRepository.listPedidoById(SecurityUtils.loggedCliente().getId());

    model.addAttribute("pedidos", pedidos);
    model.addAttribute("categorias", categorias);
    model.addAttribute("searchFilter", new SearchFilter());
    return "cliente-home";
  }

  @GetMapping(path = "/edit")
  public String edit(Model model) {

    Integer clienteId = SecurityUtils.loggedCliente().getId();
    Cliente cliente = clienteRepository.findById(clienteId).orElseThrow();
    model.addAttribute("cliente", cliente);
    ControllerHelper.setEditMode(model, true);

    return "cliente-cadastro";
  }

  @PostMapping(path = "/save")
  public String save(@ModelAttribute("cliente") @Valid Cliente cliente, Errors errors, Model model) {

    if (!errors.hasErrors()) {
      try {
        clienteService.saveCliente(cliente);
        model.addAttribute("msg", "Usario alterado com sucesso!");
      } catch (ValidationException e) {
        errors.rejectValue("email", null, e.getMessage());
      }
    }
    ControllerHelper.setEditMode(model, true);
    return "cliente-cadastro";
  }

  @GetMapping("/search")
  public String search(@ModelAttribute("searchFilter") SearchFilter filter, Model model, @RequestParam(value = "cmd", required = false) String command) {

    filter.processFilter(command);

    List<Restaurante> restaurantes = restauranteService.search(filter);
    model.addAttribute("restaurantes", restaurantes);

    ControllerHelper.addCategoriasToRequest(categoriaRestauranteRepository, model);
    model.addAttribute("cep", SecurityUtils.loggedCliente().getCep());

    model.addAttribute("searchFilter", filter);
    return "cliente-busca";
  }

  @GetMapping(path = "/restaurante")
  public String viewRestaurante(@RequestParam("restauranteId") Integer restauranteId,
                                @RequestParam(value = "categoria", required = false) String categoria,
                                Model model) {

    Restaurante restaurante = restauranteRepository.findById(restauranteId).orElseThrow();
    model.addAttribute("restaurante", restaurante);

    model.addAttribute("cep", SecurityUtils.loggedCliente().getCep());

    List<String> categorias = itemCardapioRepository.findCategorias(restauranteId);

    model.addAttribute("categorias", categorias);

    List<ItemCardapio> itensCardapioDestaque;
    List<ItemCardapio> itensCardapioNaoDestaque;

    if (categoria == null) {

      itensCardapioDestaque = itemCardapioRepository.findByRestaurante_IdAndDestaqueOrderByNome(restauranteId, true);
      itensCardapioNaoDestaque = itemCardapioRepository.findByRestaurante_IdAndDestaqueOrderByNome(restauranteId, false);

    } else {

      itensCardapioDestaque = itemCardapioRepository.findByRestaurante_IdAndDestaqueAndCategoriaOrderByNome(restauranteId, true, categoria);
      itensCardapioNaoDestaque = itemCardapioRepository.findByRestaurante_IdAndDestaqueAndCategoriaOrderByNome(restauranteId, false, categoria);

    }

    model.addAttribute("itensCardapioDestaque", itensCardapioDestaque);
    model.addAttribute("itensCardapioNaoDestaque", itensCardapioNaoDestaque);
    model.addAttribute("categoriaSelecionada", categoria);

    return "cliente-restaurante";
  }
}

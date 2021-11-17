package bluefoodApp.service;

import bluefoodApp.cliente.Cliente;
import bluefoodApp.cliente.ClienteRepository;
import bluefoodApp.restaurante.Restaurante;
import bluefoodApp.restaurante.RestauranteComparator;
import bluefoodApp.restaurante.RestauranteRepository;
import bluefoodApp.restaurante.SearchFilter;
import bluefoodApp.util.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Iterator;
import java.util.List;

@Service
public class RestauranteService {

  @Autowired
  private RestauranteRepository restauranteRepository;

  @Autowired
  private ImageService imageService;

  @Autowired
  private ClienteRepository clienteRepository;

  @Transactional
  public void saveRestaurante(Restaurante restaurante) throws ValidationException {
    if (!validateEmail(restaurante.getEmail(), restaurante.getId())) {
      throw new ValidationException("O Email está duplicado");
    }

    if (restaurante.getId() != null) {
      Restaurante restaurantedb = restauranteRepository.findById(restaurante.getId()).orElseThrow();
      restaurante.setSenha(restaurantedb.getSenha());

    } else {
      restaurante.encryptPassword();
      restaurante = restauranteRepository.save(restaurante);
      restaurante.setLogotipoFileName();
      imageService.uploadLogotipo(restaurante.getLogotipoFile(), restaurante.getLogotipo());
    }

  }

  private boolean validateEmail(String email, Integer id) {

    Cliente cliente = clienteRepository.findByEmail(email);

    if (cliente != null) {
      return false;
    }

    Restaurante restaurante = restauranteRepository.findByEmail(email);

    if (restaurante != null) {
      if (id == null) {
        return false;
      }

      if (!restaurante.getId().equals(id)) {
        return false;
      }
    }

    return true;
  }

  public List<Restaurante> search(SearchFilter filter) {
    List<Restaurante> restaurantes;

    if (filter.getSearchType() == SearchFilter.SearchType.Texto) {
      restaurantes = restauranteRepository.findByNomeIgnoreCaseContaining(filter.getTexto());
    } else if (filter.getSearchType() == SearchFilter.SearchType.Categoria) {
      restaurantes = restauranteRepository.findByCategorias_Id(filter.getCategoriaId());
    } else {
      throw new IllegalStateException("O tipo de busca " + filter.getSearchType() + " não é suportado");
    }

    Iterator<Restaurante> it = restaurantes.iterator();

    while (it.hasNext()) {
      Restaurante restaurante = it.next();
      double taxaEntrega = restaurante.getTaxaEntrega().doubleValue();

      if (filter.getEntregaGratis() && taxaEntrega > 0 || !filter.getEntregaGratis() && taxaEntrega == 0) {
        it.remove();
      }
    }

    RestauranteComparator restauranteComparator = new RestauranteComparator(filter, SecurityUtils.loggedCliente().getCep());

    restaurantes.sort(restauranteComparator);

    return restaurantes;
  }
}

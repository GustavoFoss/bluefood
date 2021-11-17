package bluefoodApp.service;

import bluefoodApp.cliente.Cliente;
import bluefoodApp.cliente.ClienteRepository;
import bluefoodApp.restaurante.Restaurante;
import bluefoodApp.restaurante.RestauranteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class ClienteService {

  @Autowired
  private ClienteRepository clienteRepository;

  @Autowired
  private RestauranteRepository restauranteRepository;

  @Transactional
  public void saveCliente(Cliente cliente) throws ValidationException {
    if (!validateEmail(cliente.getEmail(), cliente.getId())) {
      throw new ValidationException("O Email está duplicado");
    }

    if (cliente.getId() != null) {
      Cliente clientedb = clienteRepository.findById(cliente.getId()).orElseThrow();
      cliente.setSenha(clientedb.getSenha());

    } else {
      cliente.encryptPassword();
    }


    clienteRepository.save(cliente);
  }

  private boolean validateEmail(String email, Integer id) {

    Restaurante restaurante = restauranteRepository.findByEmail(email);

    if (restaurante != null) {
      return false;
    }

    Cliente cliente = clienteRepository.findByEmail(email);

    if (cliente != null) {
      if (id == null) {
        return false;
      }

      if (!cliente.getId().equals(id)) {
        return false;
      }
    }

    return true;
  }
}

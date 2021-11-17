package bluefoodApp.restaurante;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ItemCardapioRepository extends JpaRepository<ItemCardapio, Integer> {

  @Query("SELECT DISTINCT ic.categoria FROM ItemCardapio ic WHERE ic.restaurante.id = ?1 ORDER BY ic.categoria")
  public List<String> findCategorias(Integer restauranteId);

  public List<ItemCardapio> findByRestaurante_IdOrderByNome(Integer restauranteId);

  public List<ItemCardapio> findByRestaurante_IdAndDestaqueOrderByNome(Integer restaurante_id, Boolean destaque);

  public List<ItemCardapio> findByRestaurante_IdAndDestaqueAndCategoriaOrderByNome(Integer restaurante_id, Boolean destaque, String categoria);

}

package bluefoodApp.restaurante;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "categoria_restaurante")
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class CategoriaRestaurante implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @EqualsAndHashCode.Include
  private Integer id;

  @NotNull
  @Size(max = 20)
  private String nome;

  @NotNull
  @Size(max = 50)
  private String imagem;

  @ManyToMany(mappedBy = "categorias")
  private Set<Restaurante> restaurantes = new HashSet<>(0);

}

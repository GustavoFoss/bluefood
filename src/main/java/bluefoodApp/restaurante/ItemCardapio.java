package bluefoodApp.restaurante;

import bluefoodApp.controllerWeb.UploadConstraint;
import bluefoodApp.util.FileType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Getter
@Table(name = "item_cardapio")
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ItemCardapio implements Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @NotBlank(message = "Nome não pode ser vazio")
  @Size(max = 50)
  private String nome;

  @NotBlank(message = "Categoria não pode ser vazio")
  @Size(max = 25)
  private String categoria;

  @NotBlank(message = "Descrição não pode ser vazio")
  @Size(max = 80)
  private String descricao;

  @Size(max = 50)
  private String imagem;

  @NotNull(message = "O preço não pode ser vazio")
  @Min(0)
  private BigDecimal preco;

  @NotNull
  private Boolean destaque;

  @NotNull
  @ManyToOne
  @JoinColumn(name = "restaurante_id")
  private Restaurante restaurante;

  @Transient
  @UploadConstraint(acceptedTypes = FileType.PNG, message = "O arquivo não é uma imagem padrão")
  private MultipartFile imagemFile;

  public void setImagemFileName() {
    if (getId() == null) {
      throw new IllegalStateException("O objeto pricisa primeiro ser criado");
    }

    this.imagem = String.format("%04d-comida.%s", getId(), FileType.of(imagemFile.getContentType()).getExtension());
  }
}

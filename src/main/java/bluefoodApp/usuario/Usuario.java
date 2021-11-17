package bluefoodApp.usuario;

import bluefoodApp.util.StringUtils;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@MappedSuperclass
public class Usuario implements Serializable {

  @EqualsAndHashCode.Include
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @NotBlank(message = "NOME não pode ser vazio")
  @Size(max = 80, message = "NOME muito grande")
  private String nome;

  @NotBlank(message = "EMAIL não pode ser vazia")
  @Size(max = 60, message = "EMAIL muito grande")
  @Email(message = "EMAIL Invalido")
  private String email;

  @NotBlank(message = "SENHA não pode ser vazia")
  @Size(max = 80, message = "SENHA é muito grande")
  private String senha;

  @NotBlank(message = "TELEFONE não pode ser vazio")
  @Pattern(regexp = "[0-9]{10,11}", message = "TELEFONE é invalido")
  @Column(length = 11, nullable = false)
  private String telefone;

  public void encryptPassword() {
    this.senha = StringUtils.encrypt(this.senha);
  }
}

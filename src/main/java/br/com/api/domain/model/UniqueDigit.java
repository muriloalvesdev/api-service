package br.com.api.domain.model;

import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import br.com.api.dto.UniqueDigitDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "unique_digit")
public class UniqueDigit {

  @Id
  private UUID id;

  @Column
  private String username;

  @Column
  private String digit;

  @Column
  private String quantity;

  @Column(name = "unique_digit")
  private Long uniqueDigit;

  public UniqueDigitDTO build() {
    return new UniqueDigitDTO(this.username, this.digit, this.quantity,
        String.valueOf(this.uniqueDigit));
  }
}

package io.yorosoft.usermanagementapi.model;

import jakarta.persistence.*;
import lombok.*;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@SequenceGenerator(name = "refresh_token_seq_generator",
        allocationSize = 1,
        sequenceName = "refresh_token_seq")
public class RefreshToken {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE,
          generator = "refresh_token_seq_generator")
  public Long id;

  @NotBlank
  public String token;

  @NotNull
  @Column(name = "creation_date")
  private Instant creationDate;
}

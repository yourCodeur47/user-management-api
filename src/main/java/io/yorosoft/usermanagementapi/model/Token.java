package io.yorosoft.usermanagementapi.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import jakarta.validation.constraints.NotBlank;
import java.time.Instant;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
public class Token {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  public Long id;

  @NotBlank
  public String token;

  @NotNull
  @Column(name = "expiration_date")
  private Instant expirationDate;

  @OneToOne(fetch = FetchType.LAZY)
  @ToString.Exclude
  public User user;

}

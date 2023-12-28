package io.yorosoft.usermanagementapi.model;

import io.yorosoft.usermanagementapi.enums.TokenType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.EnumType;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.GenerationType;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@SequenceGenerator(name = "token_seq_generator", allocationSize = 1, sequenceName = "token_seq")
public class Token {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "token_seq_generator")
  public Integer id;

  @Column(unique = true)
  @NotBlank
  public String token;

  @Enumerated(EnumType.STRING)
  @NotBlank
  public TokenType tokenType = TokenType.BEARER;

  @NotNull
  public boolean revoked;

  @NotNull
  public boolean expired;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  @ToString.Exclude
  public User user;
}

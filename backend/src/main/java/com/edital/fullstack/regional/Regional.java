package com.edital.fullstack.regional;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;

@Entity
@Table(name = "regional")
public class Regional {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long pk;

  @Column(name = "id", nullable = false)
  private Integer regionalId;

  @Column(name = "nome", nullable = false, length = 200)
  private String nome;

  @Column(name = "ativo", nullable = false)
  private Boolean ativo;

  @Column(name = "created_at", nullable = false)
  private Instant createdAt;

  @Column(name = "updated_at", nullable = false)
  private Instant updatedAt;

  public Regional() {}

  public Regional(Integer regionalId, String nome, Boolean ativo, Instant createdAt, Instant updatedAt) {
    this.regionalId = regionalId;
    this.nome = nome;
    this.ativo = ativo;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
  }

  public Long getPk() {
    return pk;
  }

  public Integer getRegionalId() {
    return regionalId;
  }

  public void setRegionalId(Integer regionalId) {
    this.regionalId = regionalId;
  }

  public String getNome() {
    return nome;
  }

  public void setNome(String nome) {
    this.nome = nome;
  }

  public Boolean getAtivo() {
    return ativo;
  }

  public void setAtivo(Boolean ativo) {
    this.ativo = ativo;
  }

  public Instant getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Instant createdAt) {
    this.createdAt = createdAt;
  }

  public Instant getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(Instant updatedAt) {
    this.updatedAt = updatedAt;
  }
}

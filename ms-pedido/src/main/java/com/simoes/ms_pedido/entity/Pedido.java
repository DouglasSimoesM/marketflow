package com.simoes.ms_pedido.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "tb_pedido")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long clienteId;

    @NotBlank(message = "O item não pode estar vazio")
    private String item;
    private int quantidade;
    @NotNull(message = "O valor não pode ser nulo")
    private BigDecimal valor;
    private BigDecimal valorTotal;
    private String status;
    private boolean aprovado;
    private String observacao;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;
}

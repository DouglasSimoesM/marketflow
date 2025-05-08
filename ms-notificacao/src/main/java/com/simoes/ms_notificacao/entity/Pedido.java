package com.simoes.ms_notificacao.entity;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class Pedido {

    private Long id;
    private Long idUsuario;
    private Long produtoId;
    private String item;
    private int quantidade;
    private BigDecimal valor;
    private BigDecimal valorTotal;
    private String status;
    private boolean aprovado;
    private String observacao;
    private Usuario usuario;
}

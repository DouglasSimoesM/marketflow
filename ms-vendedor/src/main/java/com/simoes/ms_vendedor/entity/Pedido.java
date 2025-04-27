package com.simoes.ms_vendedor.entity;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class Pedido {

    private Long id;
    private Long clienteId;
    private Long produtoId;
    private String item;
    private int quantidade;
    private BigDecimal valor;
    private BigDecimal valorTotal;
    private String status;
    private boolean aprovado;
    private String observacao;
}
